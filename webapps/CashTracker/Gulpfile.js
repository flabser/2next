var gulp = require('gulp');
var jshint = require('gulp-jshint');
var concat = require('gulp-concat');
var rename = require('gulp-rename');
var uglify = require('gulp-uglify');
var csso = require('gulp-csso');
var emberTemplates = require('gulp-ember-templates');
var replace = require('gulp-replace');


// js lib
var js_files = ['../SharedResources/js/modernizr.js',
    '../SharedResources/nb/js/nb.build.js',
    './js/src-api/*.js',
    './js/*.js',
    '!./js/*.build.js',
    '!./js/*.min.js'
];

gulp.task('lint', function() {
    gulp.src(js_files)
        .pipe(jshint())
        .pipe(jshint.reporter('default'));
});

gulp.task('minify_js', function() {
    gulp.src(js_files)
        .pipe(concat('app.build.js'))
        .pipe(gulp.dest('./js'))
        .pipe(rename('app.min.js'))
        .pipe(uglify())
        .pipe(gulp.dest('./js'));
});
// --------------------------------

// css
var css_files = ['../SharedResources/css/normalize.css',
    './css/*.css',
    '!./css/all.min.css'
];

gulp.task('minify_css', function() {
    gulp.src(css_files)
        .pipe(concat('all.min.css'))
        .pipe(csso())
        .pipe(gulp.dest('./css'));
});
// --------------------------------

// ember app
var js_ember_files = ['./js/ember-app/**/*.js',
    '!./js/ember-app/app.build.js',
    '!./js/ember-app/app.min.js'
];

gulp.task('em_lint', function() {
    gulp.src(js_ember_files)
        .pipe(jshint())
        .pipe(jshint.reporter('default'));
});

gulp.task('em_minify_js', function() {
    gulp.src(js_ember_files)
        .pipe(concat('app.min.js'))
        .pipe(uglify())
        .pipe(gulp.dest('./js/ember-app'));
});
// --------------------------------

// ember templates
gulp.task('em_templates', function() {
    gulp.src('./js/ember-app/templates/*.html')
        .pipe(emberTemplates({
            type: 'amd',
            compiler: require('./js/lib/ember-template-compiler'),
            isHTMLBars: true
        }))
        .pipe(concat('templates.js'))
        .pipe(replace(/(\n|\s{2,})/g, ''))
        .pipe(gulp.dest('./js/ember-app/templates/compiled'));
});
// --------------------------------

// run
gulp.task('default', function() {
    gulp.run('lint', 'em_lint', 'em_templates', 'em_minify_js', 'minify_js', 'minify_css');

    gulp.watch(js_files, function(event) {
        gulp.run('minify_js');
    });

    gulp.watch(js_ember_files, function(event) {
        gulp.run('em_minify_js');
    });

    gulp.watch(css_files, function(event) {
        gulp.run('minify_css');
    });
});
