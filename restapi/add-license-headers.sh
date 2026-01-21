#!/bin/bash

# Script to add GPLv3 license headers to all Java files in restapi/src

LICENSE_HEADER="/*
 * RestApi Extension for Krista
 * Copyright (C) 2024 Krista Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */"

# Count files processed
PROCESSED=0
SKIPPED=0

echo "Adding GPLv3 license headers to Java files in restapi/src..."
echo ""

# Find all Java files and add header if not already present
find restapi/src -name "*.java" -type f | while read file; do
    if ! grep -q "GNU General Public License" "$file"; then
        echo "Adding license header to: $file"
        # Create temp file with license header + original content
        echo "$LICENSE_HEADER" > temp_file
        echo "" >> temp_file
        cat "$file" >> temp_file
        # Replace original file
        mv temp_file "$file"
        ((PROCESSED++))
    else
        echo "Skipping (already has license): $file"
        ((SKIPPED++))
    fi
done

echo ""
echo "✅ License header addition complete!"
echo "   Processed: $PROCESSED files"
echo "   Skipped: $SKIPPED files"

