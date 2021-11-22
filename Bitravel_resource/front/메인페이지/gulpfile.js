
'use strict';
var { src, dest, series, parallel, watch } = require('gulp');
var browserify = require('browserify');
var babelify = require('babelify');
var source = require('vinyl-source-stream');
var buffer = require('vinyl-buffer');
var sass = require('gulp-sass')(require('sass'));
var rename = require('gulp-rename');
var sourcemaps = require('gulp-sourcemaps');
var browsersync = require('browser-sync').create();
var uglify = require('gulp-uglify');
var cleanCSS = require('gulp-clean-css');
var del = require('del');
var glob = require('glob');
const autoprefixer = require('gulp-autoprefixer');
// Define paths
var paths = {
    here: './',
    base: {
        base: {
            dest: './'
        },
        node: {
            dest: './node_modules'
        }
    },
    src: {
        base: {
            dir: './src',
            files: './src/**/*',
            dest: './dist'
        },
        html: {
            dir: './src',
            files: './src/**/*.html',
            dest: './dist/'
        },
        img: {
            dir: './src/assets/img',
            files: './src/assets/img/**/*',
            dest: './dist/assets/img'
        },
        fonts: {
            dir: './src/assets/fonts',
            files: './src/assets/fonts/**/*',
            dest: './dist/assets/fonts'
        },
        videos: {
            dir: './src/assets/videos',
            files: './src/assets/videos/**/*',
            dest: './dist/assets/videos'
        },
        js: {
            dir: './src/js',
            files: './src/js/custom/**/*.js',
            theme: './src/js/theme.js',
            dest: './src/assets/js'
        },
        scss: {
            dir: './src/scss',
            files: './src/scss/**/*',
            main: './src/scss/*.scss',
            dest: './src/assets/css'
        }
    }
};

function browserSync(done) {
    browsersync.init({
        server: {
            baseDir: [paths.src.base.dir]
        },
    });
    done();
};

function browsersyncReload(done) {
    browsersync.reload();
    done();
};

function bundleJs() {
    var files = glob.sync('./src/js/custom/**/*.js');
    return (
        browserify({
            entries: files,
            debug: true,
            cache: {},
            packageCache: {}
        }).transform(babelify, {
            global: true,
            presets: ["@babel/preset-env"]
        })
            .bundle()
            .pipe(source('theme.bundle.js'))
            .pipe(buffer())
            .pipe(sourcemaps.init())
            .pipe(uglify())
            .pipe(sourcemaps.write(paths.here))
            .pipe(dest(paths.src.js.dest))
    );
};
//styles
function buildCss() {
    return src(paths.src.scss.main)
        .pipe(sourcemaps.init())
        .pipe(sass.sync().on('error', sass.logError))
        .pipe(autoprefixer())
        .pipe(sourcemaps.write(paths.here))
        .pipe(dest(paths.src.scss.dest))
        .pipe(browsersync.stream());
};

function minifyCss() {
    return src(paths.src.scss.main)
        .pipe(sourcemaps.init())
        .pipe(sass.sync().on('error', sass.logError))
        .pipe(autoprefixer())
        .pipe(cleanCSS())
        .pipe(rename({
            suffix: '.min'
        }))
        .pipe(sourcemaps.write(paths.here))
        .pipe(dest(paths.src.scss.dest))
        .pipe(browsersync.stream());
};



function cleanUp() {
    return del([paths.src.js.dest, paths.src.scss.dest]);
};
function watchFiles() {
    watch(paths.src.scss.files, series(buildCss, minifyCss));
    watch(paths.src.js.files, series(bundleJs, browsersyncReload));
    watch(paths.src.html.files, series(browsersyncReload));
};

exports.watchFiles = watch;
exports.buildCss = buildCss;
exports.bundleJs = bundleJs;
exports.minifyCss = minifyCss;
exports.default = series(cleanUp, buildCss, minifyCss, bundleJs, parallel(browserSync, watchFiles));