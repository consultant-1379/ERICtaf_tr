/*global require, process*/
var fs = require("fs");

var args = process.argv,
    file = args[2],
    version = args[3],
    deployDate = args[4];

var content = fs.readFileSync(file).toString();
var replaced = content.replace("$VERSION$", version);
replaced = replaced.replace("$DATE$", deployDate);

fs.writeFileSync(file, replaced);
fs.writeSync(1, "Filename: " + file);
fs.writeSync(1, "Replaced version property with: " + version);
process.exit();