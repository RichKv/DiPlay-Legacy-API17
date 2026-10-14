#!/usr/bin/env python3
# SPDX-License-Identifier: GPL-3.0-only
"""Bounded source/history scan; report paths/categories only, never matched values."""
from pathlib import Path
import re
import subprocess
import sys

ROOT = Path(__file__).resolve().parents[1]
BLOCKED_SUFFIXES = {
    '.pk8', '.p7b', '.pem', '.key', '.p12', '.pfx', '.jks', '.keystore', '.apk', '.aab',
}
PRIVATE_PATH = re.compile(
    r'(^|/)(?:\.private|credential[s]?|pairing-records)(/|$)|'
    r'(^|/)release-signing\.properties$',
    re.IGNORECASE,
)
PATTERNS = {
    'PEM private-key block': re.compile(
        rb'-----BEGIN (?:[A-Z0-9 ]+ )?PRIVATE KEY-----\s+[A-Za-z0-9+/=\r\n]{40,}'
    ),
    'common GitHub token': re.compile(
        rb'(?<![A-Za-z0-9_])(?:gh[pousr]_[A-Za-z0-9]{30,}|github_pat_[A-Za-z0-9_]{40,})'
    ),
    'AWS access-key pattern': re.compile(rb'(?<![A-Z0-9])(?:AKIA|ASIA)[A-Z0-9]{16}(?![A-Z0-9])'),
    'credential-bearing HTTP URL': re.compile(rb'https?://[^\s"<>/@:]{1,100}:[^\s"<>/@]{1,200}@'),
}


def git(*args):
    return subprocess.check_output(['git', *args], cwd=ROOT)


def categories(name, data):
    result = []
    if Path(name).suffix.lower() in BLOCKED_SUFFIXES or PRIVATE_PATH.search(name):
        result.append('forbidden credential/distribution/private-platform path')
    result.extend(label for label, pattern in PATTERNS.items() if pattern.search(data))
    return result


def main():
    failures = set()
    current = git('ls-files', '--cached', '--others', '--exclude-standard', '-z').decode().split('\0')
    for name in filter(None, current):
        path = ROOT / name
        if path.is_file():
            for category in categories(name, path.read_bytes()):
                failures.add(('working tree', name, category))

    commits = git('rev-list', 'HEAD').decode().splitlines()
    blobs = {}
    history_file_versions = 0
    for commit in commits:
        for record in git('ls-tree', '-r', '-z', commit).split(b'\0'):
            if not record:
                continue
            meta, path = record.split(b'\t', 1)
            _, kind, oid = meta.decode().split()
            if kind != 'blob':
                continue
            name = path.decode()
            history_file_versions += 1
            blobs.setdefault(oid, set()).add(name)

    with subprocess.Popen(
        ['git', 'cat-file', '--batch'], cwd=ROOT,
        stdin=subprocess.PIPE, stdout=subprocess.PIPE,
    ) as process:
        for oid, names in blobs.items():
            process.stdin.write((oid + '\n').encode())
            process.stdin.flush()
            header = process.stdout.readline().decode().split()
            if len(header) != 3 or header[1] != 'blob':
                raise RuntimeError('Unexpected Git blob response; no content emitted')
            data = process.stdout.read(int(header[2]))
            if process.stdout.read(1) != b'\n':
                raise RuntimeError('Unexpected Git batch boundary; no content emitted')
            for name in names:
                for category in categories(name, data):
                    failures.add(('source history', name, category))
        process.stdin.close()

    if failures:
        for scope, name, category in sorted(failures):
            print(f'{scope}: {name}: {category}', file=sys.stderr)
        return 1
    print(
        f'Source boundary check passed: current source plus {len(commits)} HEAD commits, '
        f'{history_file_versions} historical file versions; selected patterns only.'
    )
    return 0


if __name__ == '__main__':
    raise SystemExit(main())
