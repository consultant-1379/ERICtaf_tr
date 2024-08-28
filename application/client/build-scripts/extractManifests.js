var fs = require('fs');
var paths = {
    build: __dirname + '/../build.json',
    jshintrc: __dirname + '/../src/.jshintrc'
};

var build = require(paths.build);

console.log('Extracting JSHint manifest...');
var jshint = build.phases['code-verify'].jshint;
fs.writeFileSync(paths.jshintrc, JSON.stringify(jshint, null, 2));
