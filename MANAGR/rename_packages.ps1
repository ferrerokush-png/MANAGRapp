# PowerShell script to rename packages from com.example.releaseflow.personal to com.managr.app

$rootPath = "app\src\main\java"

# Find all Kotlin files
$files = Get-ChildItem -Path $rootPath -Filter "*.kt" -Recurse

Write-Host "Found $($files.Count) Kotlin files to update"

foreach ($file in $files) {
    $content = Get-Content -Path $file.FullName -Raw
    $modified = $false
    
    # Replace package declarations
    if ($content -match "package com\.example\.releaseflow\.personal") {
        $content = $content -replace "package com\.example\.releaseflow\.personal", "package com.managr.app"
        $modified = $true
    }
    
    # Replace imports
    if ($content -match "import com\.example\.releaseflow\.personal") {
        $content = $content -replace "import com\.example\.releaseflow\.personal", "import com.managr.app"
        $modified = $true
    }
    
    if ($modified) {
        Set-Content -Path $file.FullName -Value $content -NoNewline
        Write-Host "Updated: $($file.Name)"
    }
}

Write-Host "`nPackage renaming complete!"
Write-Host "Now you need to manually move the directory structure from:"
Write-Host "  com/example/releaseflow/personal"
Write-Host "to:"
Write-Host "  com/managr/app"

