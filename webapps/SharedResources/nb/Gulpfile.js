var gulp = require('gulp');
var jshint = require('gulp-jshint');
var concat = require('gulp-concat');
var rename = require('gulp-rename');
var uglify = require('gulp-uglify');

var js_files = ['js/src/nb.js', 'js/src/*.js'];

gulp.task('lint', function() {
	gulp.src(js_files)
		.pipe(jshint())
		.pipe(jshint.reporter('default'));
});

gulp.task('minify_js', function() {
	gulp.src(js_files)
		.pipe(concat('nb.build.js'))
		.pipe(gulp.dest('./js'));
});

gulp.task('default', function() {
	gulp.run('lint', 'minify_js');

	gulp.watch(js_files, function(event) {
		gulp.run('minify_js');
	});
});
