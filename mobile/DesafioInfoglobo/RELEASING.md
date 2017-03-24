Release Process
===============

Release
-------

1. Update the `CHANGELOG.md` file describing the internal and/or public changes.

2. Create `releasing` branch.

3. Commit the changes with `git commit -m "Preparing version X.Y.Z."`, replacing 'X.Y.Z' with the
    number of the new version.

4. Push `releasing` branch and open pull request. Source/Target: `releasing` -> `alpha`.

5. Pull `alpha` branch and push tag 'X.Y.Z'

6. Merge `releasing` into `master` branch with commit message: "Preparing next development version.".