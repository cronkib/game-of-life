function seedgen{param([int]$Grid) for($i = 0; $i -lt $Grid; $i++){[string]$line = ""; for($x = 0; $x -lt $Grid; $x++){[string]$line = $line + (Get-Random -Maximum 2 -Minimum 0)};$line}}
seedgen(20)