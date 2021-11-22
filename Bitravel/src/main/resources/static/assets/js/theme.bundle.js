<!doctype html>
<html lang="en">

    <head>
        <!-- Required meta tags -->
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="icon" href="assets/img/favicon.ico" type="image/ico">
        <!--Icons-->
        <link href="assets/fonts/iconsmind/iconsmind.css" rel="stylesheet">
        <link href="assets/fonts/bootstrap-icons/bootstrap-icons.css" rel="stylesheet"><!--Google fonts-->        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link
            href="https://fonts.googleapis.com/css2?family=DM+Serif+Display:ital@0;1&family=Poppins:wght@200;300;400;500;600;700&display=swap"
            rel="stylesheet">
        <!--Flatpickr-->
        <link rel="stylesheet" href="assets/vendor/flatpickr/flatpickr.min.css">
        <!-- Quill editor stylesheet -->
        <link href="https://cdn.quilljs.com/1.3.6/quill.snow.css" rel="stylesheet">

        <!--File uploader-->
        <link href="https://unpkg.com/filepond/dist/filepond.css" rel="stylesheet" />
        <link href="https://unpkg.com/filepond-plugin-file-poster/dist/filepond-plugin-file-poster.css" rel="stylesheet">
        <link href="https://unpkg.com/filepond-plugin-image-preview/dist/filepond-plugin-image-preview.css" rel="stylesheet">
        <!-- Main CSS -->
        <link href="assets/css/theme.min.css" rel="stylesheet">

        <title>예시</title>
    </head>

    <body>
        <!--Preloader Spinner-->
        <!-- <div class="spinner-loader bg-gradient-secondary text-white">
            <div class="spinner-border text-primary" role="status">
            </div>
            <span class="small d-block ms-2">Loading...</span>
        </div> -->
        <!--Header Start-->
        <header class="z-index-fixed">
            <nav class="navbar navbar-expand-lg navbar-light bg-white">
                <div class="container position-relative">
                    <a class="navbar-brand" href="index.html">
                        <img src="assets/img/logo/logo.svg" alt="" class="img-fluid">
                    </a>

                    <div class="d-flex align-items-center navbar-no-collapse-items order-lg-last">
                        <button class="navbar-toggler order-last" type="button" data-bs-toggle="collapse"
                            data-bs-target="#mainNavbarTheme" aria-controls="mainNavbarTheme" aria-expanded="false"
                            aria-label="Toggle navigation">
                            <span class="navbar-toggler-icon">
                                <i></i>
                            </span>
                        </button>
                        <div class="nav-item me-3 me-lg-0 dropdown">
                            <a href="#" class="btn btn-secondary rounded-pill py-0 ps-0 pe-3"
                                data-bs-auto-close="outside" data-bs-toggle="dropdown">
                                <img src="assets/img/avatar/4.jpg" alt="" class="avatar sm rounded-circle me-1">
                                <small>Emily</small>
                            </a>
                            <div class="dropdown-menu dropdown-menu-end dropdown-menu-xs p-0">
                                <a href="#" class="dropdown-header border-bottom p-4">
                                    <div class="d-flex align-items-center">
                                        <div>
                                            <img src="assets/img/avatar/4.jpg" alt=""
                                                class="avatar xl rounded-pill me-3">
                                        </div>
                                        <div>
                                            <h5 class="mb-0">Emily doe</h5>
                                            <span class="text-muted d-block mb-2">emily@domain.com</span>
                                            <div class="small d-inline-block link-underline fw-semibold text-muted">View
                                                account</div>
                                        </div>
                                    </div>
                                </a>
                                <a href="#" class="dropdown-item p-3">
                                    <span class="d-block text-end">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20"
                                            fill="currentColor" class="bi bi-box-arrow-right me-2" viewBox="0 0 16 16">
                                            <path fill-rule="evenodd"
                                                d="M10 12.5a.5.5 0 0 1-.5.5h-8a.5.5 0 0 1-.5-.5v-9a.5.5 0 0 1 .5-.5h8a.5.5 0 0 1 .5.5v2a.5.5 0 0 0 1 0v-2A1.5 1.5 0 0 0 9.5 2h-8A1.5 1.5 0 0 0 0 3.5v9A1.5 1.5 0 0 0 1.5 14h8a1.5 1.5 0 0 0 1.5-1.5v-2a.5.5 0 0 0-1 0v2z">
                                            </path>
                                            <path fill-rule="evenodd"
                                                d="M15.854 8.354a.5.5 0 0 0 0-.708l-3-3a.5.5 0 0 0-.708.708L14.293 7.5H5.5a.5.5 0 0 0 0 1h8.793l-2.147 2.146a.5.5 0 0 0 .708.708l3-3z">
                                            </path>
                                        </svg>
                                        Sign Out
                                    </span>
                                </a>
                            </div>
                        </div>
                    </div>
                    <div class="collapse navbar-collapse" id="mainNavbarTheme">
                        <ul class="navbar-nav mx-auto">
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle" href="index.html" role="button"
                                    data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Landings
                                    <!--Dropdown menu arrow svg icon-->
                                    <svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 13 9' class="dropdown-arrow">
                                        <path fill='currentColor'
                                            d="M12.25 2.30062L10.8988 0.949371L6.5 5.33854L2.10125 0.949371L0.75 2.30062L6.5 8.05062L12.25 2.30062Z">
                                        </path>
                                    </svg>
                                </a>
                                <div class="dropdown-menu dropdown-menu-md dropdown-menu-start py-0 overflow-hidden">
                                    <div class="row">
                                        <div class="col-lg-5 ps-lg-4 position-relative">
                                            <div class="py-1 py-lg-3 d-lg-flex flex-column">
                                                <a href="index.html" class="dropdown-item">Welcome</a>
                                                <a href="index-landing-classic.html" class="dropdown-item">Classic
                                                    Landing</a>
                                                <a href="index-landing-creative.html" class="dropdown-item">Creative</a>
                                                <a href="index-landing-agency.html" class="dropdown-item">Agency</a>
                                                <a href="index-landing-business.html" class="dropdown-item">Business</a>
                                                <a href="index-landing-startup.html" class="dropdown-item">Startup</a>
                                                <a href="index-landing-consultancy.html"
                                                    class="dropdown-item">Consultancy</a>
                                                <a href="index-landing-saas-webapp.html" class="dropdown-item">Saas
                                                    WebApp</a>
                                                <a href="index-landing-mobile-App.html" class="dropdown-item">Mobile
                                                    App</a>
                                                <a href="index-landing-personal-portfolio.html"
                                                    class="dropdown-item">Personal Portfolio</a>
                                                <a href="index-landing-dark.html" class="dropdown-item">UI
                                                    Dark</a>
                                            </div>
                                        </div>
                                        <!--/.col-->
                                        <div class="col-lg-7 d-none d-lg-block position-relative bg-no-repeat bg-cover bg-center"
                                            style="background-image: url('assets/img/900x1000/2.jpg')">
                                            <div
                                                class="position-absolute w-100 h-100 top-0 start-0 bg-gradient-primary opacity-80">
                                            </div>
                                            <div
                                                class="p-4 d-flex flex-column align-items-center text-center justify-content-center h-100 py-5 position-relative text-white">
                                                <h5 class="display-6 mb-4">Ultimate modern design toolkit</h5>
                                                <p class="mb-0">Built with lots of love and patient</p>
                                                <p>
                                            </div>
                                        </div>
                                        <!--/.col-->
                                    </div>
                                    <!--/.row-->
                                </div>
                            </li>
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown"
                                    aria-haspopup="true" aria-expanded="false" data-bs-auto-close="outside">Portfolio
                                    <!--Dropdown menu arrow svg icon-->
                                    <svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 13 9' class="dropdown-arrow">
                                        <path fill='currentColor'
                                            d="M12.25 2.30062L10.8988 0.949371L6.5 5.33854L2.10125 0.949371L0.75 2.30062L6.5 8.05062L12.25 2.30062Z">
                                        </path>
                                    </svg>
                                </a>
                                <div class="dropdown-menu">
                                    <div class="dropend">
                                        <a class="dropdown-item dropdown-toggle" data-bs-toggle="dropdown"
                                            href="#">Classic</a>
                                        <div class="dropdown-menu">
                                            <a class="dropdown-item" href="portfolio-classic-2-col.html">2 Col</a>
                                            <a class="dropdown-item" href="portfolio-classic-3-col.html">3 Col</a>
                                            <a class="dropdown-item" href="portfolio-classic-4-col.html">4 Col</a>
                                            <a class="dropdown-item" href="portfolio-classic-masonry.html">Masonry</a>
                                        </div>
                                    </div>
                                    <div class="dropend">
                                        <a class="dropdown-item dropdown-toggle" data-bs-toggle="dropdown" href="#">Grid
                                            Overlay</a>
                                        <div class="dropdown-menu">
                                            <a class="dropdown-item" href="portfolio-grid-overlay-2-col.html">2 Col</a>
                                            <a class="dropdown-item" href="portfolio-grid-overlay-3-col.html">3 Col</a>
                                            <a class="dropdown-item" href="portfolio-grid-overlay-4-col.html">4 Col</a>
                                            <a class="dropdown-item"
                                                href="portfolio-grid-overlay-masonry.html">Masonry</a>
                                        </div>
                                    </div>
                                    <div class="dropend">
                                        <a class="dropdown-item dropdown-toggle" data-bs-toggle="dropdown" href="#">Full
                                            width</a>
                                        <div class="dropdown-menu">
                                            <a class="dropdown-item" href="portfolio-full-width-3-col.html">3 Col</a>
                                            <a class="dropdown-item" href="portfolio-full-width-4-col.html">4 Col</a>
                                            <a class="dropdown-item"
                                                href="portfolio-full-width-masonry.html">Masonry</a>
                                        </div>
                                    </div>
                                    <div class="dropend">
                                        <a class="dropdown-item dropdown-toggle" data-bs-toggle="dropdown"
                                            href="#">Project
                                            Layouts</a>
                                        <div class="dropdown-menu">
                                            <a class="dropdown-item" href="portfolio-case-study.html">Case Study</a>
                                            <a class="dropdown-item" href="portfolio-single-classic.html">classic</a>
                                        </div>
                                    </div>
                                </div>
                            </li>
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown"
                                    aria-haspopup="true" aria-expanded="false" data-bs-auto-close="outside">blog
                                    <!--Dropdown menu arrow svg icon-->
                                    <svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 13 9' class="dropdown-arrow">
                                        <path fill='currentColor'
                                            d="M12.25 2.30062L10.8988 0.949371L6.5 5.33854L2.10125 0.949371L0.75 2.30062L6.5 8.05062L12.25 2.30062Z">
                                        </path>
                                    </svg>
                                </a>
                                <div class="dropdown-menu">
                                    <div class="dropend">
                                        <a class="dropdown-item dropdown-toggle" data-bs-toggle="dropdown" href="#">Blog
                                            Layouts</a>
                                        <div class="dropdown-menu">
                                            <a class="dropdown-item" href="blog-classic.html">Blog classic</a>
                                            <a class="dropdown-item" href="blog-standard.html">Blog standard</a>
                                            <a class="dropdown-item" href="blog-masonry.html">Blog Masonry</a>
                                            <a class="dropdown-item" href="blog-sidebar.html">Blog Sidebar</a>
                                            <a class="dropdown-item" href="blog-magazine.html">Blog magazine</a>
                                        </div>
                                    </div>
                                    <div class="dropend">
                                        <a class="dropdown-item dropdown-toggle" data-bs-toggle="dropdown"
                                            href="#">Article
                                            Layouts</a>
                                        <div class="dropdown-menu">
                                            <a class="dropdown-item" href="blog-article-basic.html">Basic</a>
                                            <a class="dropdown-item" href="blog-article-video.html">Video</a>
                                            <a class="dropdown-item" href="blog-article-gallery.html">Gallery</a>
                                            <a class="dropdown-item" href="blog-article-parallax.html">Parallax
                                                Header</a>
                                        </div>
                                    </div>
                                </div>
                            </li>
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle active" href="#" role="button"
                                    data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false"
                                    data-bs-auto-close="outside">Pages
                                    <!--Dropdown menu arrow svg icon-->
                                    <svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 13 9' class="dropdown-arrow">
                                        <path fill='currentColor'
                                            d="M12.25 2.30062L10.8988 0.949371L6.5 5.33854L2.10125 0.949371L0.75 2.30062L6.5 8.05062L12.25 2.30062Z">
                                        </path>
                                    </svg>
                                </a>
                                <div class="dropdown-menu">
                                    <div class="dropend">
                                        <a class="dropdown-item dropdown-toggle" data-bs-toggle="dropdown"
                                            href="#">About</a>
                                        <div class="dropdown-menu">
                                            <a class="dropdown-item" href="page-about.html">About Company</a>
                                            <a class="dropdown-item" href="page-about-modern.html">About modern</a>
                                            <a class="dropdown-item" href="page-team.html">Our Team</a>
                                        </div>
                                    </div>
                                    <div class="dropend">
                                        <a class="dropdown-item dropdown-toggle" href="#"
                                            data-bs-toggle="dropdown">Services</a>
                                        <div class="dropdown-menu">
                                            <a class="dropdown-item" href="page-services.html">Services</a>
                                            <a class="dropdown-item" href="page-services-modern.html">Services
                                                Modern</a>
                                        </div>
                                    </div>
                                    <div class="dropend">
                                        <a class="dropdown-item dropdown-toggle" data-bs-toggle="dropdown"
                                            href="#">Career</a>
                                        <div class="dropdown-menu">
                                            <a class="dropdown-item" href="page-careers.html">careers</a>
                                            <a class="dropdown-item" href="page-career-single.html">career single</a>
                                        </div>
                                    </div>
                                    <div class="dropend">
                                        <a class="dropdown-item dropdown-toggle" data-bs-toggle="dropdown"
                                            href="#">Customers</a>
                                        <div class="dropdown-menu">
                                            <a class="dropdown-item" href="page-customers.html">Customers</a>
                                            <a class="dropdown-item" href="page-customer-story.html">Customer Story</a>
                                        </div>
                                    </div>
                                    <div class="dropend">
                                        <a class="dropdown-item dropdown-toggle" data-bs-toggle="dropdown"
                                            href="#">Account</a>
                                        <div class="dropdown-menu">
                                            <a class="dropdown-item" href="page-profile.html">Profile</a>
                                            <a class="dropdown-item" href="page-account-signin.html">Sign In</a>
                                            <a class="dropdown-item" href="page-account-signin-alt.html">Sign In alt</a>
                                            <a class="dropdown-item" href="page-account-signup.html">Sign Up</a>
                                            <a class="dropdown-item" href="page-account-signup-alt.html">Sign Up Alt</a>
                                            <a class="dropdown-item" href="page-account-forget-password.html">Forget
                                                Password</a>
                                        </div>
                                    </div>
                                    <div class="dropend">
                                        <a class="dropdown-item dropdown-toggle" data-bs-toggle="dropdown"
                                            href="#">Misc</a>
                                        <div class="dropdown-menu">
                                            <a class="dropdown-item" href="page-misc-error-404.html">Error 404</a>
                                            <a class="dropdown-item" href="page-misc-coming-soon.html">Coming Soon</a>
                                            <a class="dropdown-item" href="page-misc-maintenance.html">Maintenance</a>
                                            <a class="dropdown-item" href="page-misc-success-message.html">Success
                                                Message</a>
                                            <a class="dropdown-item" href="page-misc-policy.html">Privacy Policy</a>
                                        </div>
                                    </div>
                                    <a class="dropdown-item" href="page-pricing.html">Pricing Plans</a>
                                    <a class="dropdown-item" href="page-faq.html">Faq</a>
                                    <a class="dropdown-item" href="page-knowladge-base.html">Knowladge Base</a>
                                    <div class="dropend">
                                        <a class="dropdown-item dropdown-toggle" data-bs-toggle="dropdown"
                                            href="#">Contact</a>
                                        <div class="dropdown-menu">
                                            <a class="dropdown-item" href="page-contact-us-1.html">Contact Us 1</a>
                                            <a class="dropdown-item" href="page-contact-us-2.html">Contact Us 2</a>
                                        </div>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>
        </header>
        <!--Main content-->

        

        <!--Footer Start-->
        <footer id="footer-default" class="bg-dark text-white position-relative">
            <div class="container py-7 py-lg-9 position-relative z-index-1">
                <div class="row">

                    <!--Footer col-->
                    <div class="col-md-9 col-lg-3 mb-5 mb-lg-0">
                        <div class="d-flex flex-column h-100 justify-content-between">
                            <a href="#" class="width-90 mb-4 mb-lg-0 d-block">
                                <img src="assets/img/logo/logo-white.svg" alt="" class="img-fluid">
                            </a>
                            <div class="mt-auto">
                                <ul class="list-inline mb-0">
                                    <li class="list-inline-item">
                                        <a href="#">
                                            <svg fill="currentColor" width="24" height="24"
                                                xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512">
                                                <path
                                                    d="M426.8 64H85.2C73.5 64 64 73.5 64 85.2v341.6c0 11.7 9.5 21.2 21.2 21.2H256V296h-45.9v-56H256v-41.4c0-49.6 34.4-76.6 78.7-76.6 21.2 0 44 1.6 49.3 2.3v51.8h-35.3c-24.1 0-28.7 11.4-28.7 28.2V240h57.4l-7.5 56H320v152h106.8c11.7 0 21.2-9.5 21.2-21.2V85.2c0-11.7-9.5-21.2-21.2-21.2z">
                                                </path>
                                            </svg>
                                        </a>
                                    </li>
                                    <li class="list-inline-item ms-2">
                                        <a href="#">
                                            <svg fill="currentColor" width="24" height="24"
                                                xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512">
                                                <path xmlns="http://www.w3.org/2000/svg"
                                                    d="M492 109.5c-17.4 7.7-36 12.9-55.6 15.3 20-12 35.4-31 42.6-53.6-18.7 11.1-39.4 19.2-61.5 23.5C399.8 75.8 374.6 64 346.8 64c-53.5 0-96.8 43.4-96.8 96.9 0 7.6.8 15 2.5 22.1-80.5-4-151.9-42.6-199.6-101.3-8.3 14.3-13.1 31-13.1 48.7 0 33.6 17.2 63.3 43.2 80.7-16-.4-31-4.8-44-12.1v1.2c0 47 33.4 86.1 77.7 95-8.1 2.2-16.7 3.4-25.5 3.4-6.2 0-12.3-.6-18.2-1.8 12.3 38.5 48.1 66.5 90.5 67.3-33.1 26-74.9 41.5-120.3 41.5-7.8 0-15.5-.5-23.1-1.4C62.8 432 113.7 448 168.3 448 346.6 448 444 300.3 444 172.2c0-4.2-.1-8.4-.3-12.5C462.6 146 479 129 492 109.5z">
                                                </path>
                                            </svg>
                                        </a>
                                    </li>
                                    <li class="list-inline-item ms-2">
                                        <a href="#">
                                            <svg fill="currentColor" width="24" height="24"
                                                xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512">
                                                <path
                                                    d="M336 96c21.2 0 41.3 8.4 56.5 23.5S416 154.8 416 176v160c0 21.2-8.4 41.3-23.5 56.5S357.2 416 336 416H176c-21.2 0-41.3-8.4-56.5-23.5S96 357.2 96 336V176c0-21.2 8.4-41.3 23.5-56.5S154.8 96 176 96h160m0-32H176c-61.6 0-112 50.4-112 112v160c0 61.6 50.4 112 112 112h160c61.6 0 112-50.4 112-112V176c0-61.6-50.4-112-112-112z">
                                                </path>
                                                <path
                                                    d="M360 176c-13.3 0-24-10.7-24-24s10.7-24 24-24c13.2 0 24 10.7 24 24s-10.8 24-24 24zM256 192c35.3 0 64 28.7 64 64s-28.7 64-64 64-64-28.7-64-64 28.7-64 64-64m0-32c-53 0-96 43-96 96s43 96 96 96 96-43 96-96-43-96-96-96z">
                                                </path>
                                            </svg>
                                        </a>
                                    </li>
                                    <li class="list-inline-item ms-2">
                                        <a href="#">
                                            <svg width="24" height="24" fill="currentColor" viewBox="0 0 512 512">
                                                <path
                                                    d="M344.1 233.6c-28.9 0-32.9 28.8-32.9 28.8h61.4s.4-28.8-28.5-28.8zM204.8 262.4h-54.4v50h51.7c7.8-.2 22.4-2.4 22.4-24.3 0-26-19.7-25.7-19.7-25.7z">
                                                </path>
                                                <path
                                                    d="M256 32C132.3 32 32 132.3 32 256s100.3 224 224 224 224-100.3 224-224S379.7 32 256 32zm47.2 137.6h77.1v23h-77.1v-23zm-39 120.8c0 57-59.4 55.2-59.4 55.2h-97.2v-187h97.2c29.6 0 52.9 16.3 52.9 49.8S229.2 244 229.2 244c37.6 0 35 46.4 35 46.4zm144.2-3.1h-96.9c0 34.7 32.9 32.5 32.9 32.5 31.1 0 30-20.1 30-20.1h32.9c0 53.4-64 49.7-64 49.7-76.7 0-71.8-71.5-71.8-71.5s-.1-71.8 71.8-71.8c75.7.1 65.1 81.2 65.1 81.2z">
                                                </path>
                                                <path
                                                    d="M218 211.3c0-19.4-13.2-19.4-13.2-19.4h-54.4v41.7h51c8.8 0 16.6-2.9 16.6-22.3z">
                                                </path>
                                            </svg>
                                        </a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <!--Footer col-->
                    <div class="col-lg-3 col-xl-2 col-md-4 col-sm-6 ms-lg-auto mb-5 mb-md-0">
                        <h5 class="mb-4">Services</h5>
                        <ul class="nav flex-column">
                            <li class="nav-item">
                                <a href="#" class="nav-link">
                                    Design
                                </a>
                            </li>
                            <li class="nav-item">
                                <a href="#" class="nav-link">
                                    Development
                                </a>
                            </li>
                            <li class="nav-item">
                                <a href="#" class="nav-link">
                                    E-commerce
                                </a>
                            </li>
                            <li class="nav-item">
                                <a href="#" class="nav-link">
                                    Mobile app
                                </a>
                            </li>
                            <li class="nav-item">
                                <a href="#" class="nav-link">
                                    Help center
                                </a>
                            </li>
                        </ul>
                    </div>
                    <!--Footer col-->
                    <div class="col-lg-3 col-xl-2 col-md-4 col-sm-6 ms-lg-auto mb-5 mb-md-0">
                        <h5 class="mb-4">About</h5>
                        <ul class="nav flex-column">
                            <li class="nav-item">
                                <a href="#" class="nav-link">
                                    Company
                                </a>
                            </li>
                            <li class="nav-item">
                                <a href="#" class="nav-link">
                                    Our blog
                                </a>
                            </li>
                            <li class="nav-item">
                                <a href="#" class="nav-link">
                                    Contact us
                                </a>
                            </li>

                            <li class="nav-item">
                                <a href="#" class="nav-link">
                                    Career
                                </a>
                            </li>
                            <li class="nav-item">
                                <a href="#" class="nav-link">
                                    Customers
                                </a>
                            </li>
                        </ul>
                    </div>
                    <!--Footer col-->
                    <div class="col-md-4 col-sm-8 col-lg-3">
                        <div class="d-flex flex-column justify-content-md-between h-100">
                            <div class="text-muted">
                                <p class="mb-2">
                                    <strong>Company, Inc.</strong>
                                </p>
                                <p class="mb-3">
                                    1355 Market St, Suite 900<br> San Francisco<br> CA 94103
                                </p>
                            </div>

                            <div class="mt-auto mb-0">
                                <a href="mailto:mail@domain.com">
                                    <strong>support@domain.com</strong>
                                </a>
                                <hr class="my-3">
                                <a href="tel:+011234567890">
                                    <strong>+01 123 456 7890</strong>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
                <hr>
                <div class="d-sm-flex flex-sm-row justify-content-center justify-content-sm-between">
                    <ul class="list-inline">
                        <li class="list-inline-item">
                            <a href="#" class="text-muted small">
                                Privacy policy
                            </a>
                        </li>
                        <li class="list-inline-item">
                            <a href="#" class="text-muted small">
                                Terms & conditions
                            </a>
                        </li>
                    </ul>

                    <small class="text-sm-end text-muted">
                        © Copyright. All Right Reserved.<br> Made by Creative DM
                    </small>
                </div>
            </div>
            <!--container-->
        </footer>

        <!-- begin Back to Top button -->
        <a href="#" class="toTop">
            <svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 13 9' class="align-middle rotate-180" width="13"
                height="9">
                <path fill='currentColor'
                    d="M12.25 2.30062L10.8988 0.949371L6.5 5.33854L2.10125 0.949371L0.75 2.30062L6.5 8.05062L12.25 2.30062Z">
                </path>
            </svg>
        </a>
        <!-- scripts -->
        <script src="assets/js/theme.bundle.js"></script>


    </body>
</html>
