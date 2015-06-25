var gulp = require('gulp');
var concat = require('gulp-concat');
var rename = require('gulp-rename');
var uglify = require('gulp-uglify');
var csso = require('gulp-csso');
var emberTemplates = require('gulp-ember-templates');
var replace = require('gulp-replace');


// css
var css_files = ['../SharedResources/css/normalize.css',
    './css/**/*.css',
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
var js_ember_files = ['./js/**/*.js',
    '!./js/libs/**/*.js',
    '!./js/app.build.js',
    '!./js/app.min.js'
];

gulp.task('em_minify_js', function() {
    gulp.src(js_ember_files)
        .pipe(concat('app.build.js'))
        .pipe(gulp.dest('./js'))
        .pipe(concat('app.min.js'))
        .pipe(uglify())
        .pipe(gulp.dest('./js'));
});
// --------------------------------

var em_templates = ['./js/templates/**/*.html'];

gulp.task('em_templates_compile', function() {
    gulp.src(em_templates)
        .pipe(emberTemplates({
            type: 'browser',
            compiler: require('../SharedResources/vendor/ember/ember-template-compiler.min'),
            isHTMLBars: true,
            name: {
                replace: '\\',
                with: '/'
            }
        }))
        .pipe(concat('templates.js'))
        .pipe(gulp.dest('./js/templates/compiled'));
});
// --------------------------------

// run
gulp.task('default', function() {
    gulp.run('em_templates_compile', 'em_minify_js', 'minify_css');

    gulp.watch(em_templates, function(event) {
        gulp.run('em_templates_compile');
    });

    gulp.watch(js_ember_files, function(event) {
        gulp.run('em_minify_js');
    });

    gulp.watch(css_files, function(event) {
        gulp.run('minify_css');
    });
});
