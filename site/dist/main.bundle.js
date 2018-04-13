webpackJsonp(["main"],{

/***/ "./src/$$_lazy_route_resource lazy recursive":
/***/ (function(module, exports) {

function webpackEmptyAsyncContext(req) {
	// Here Promise.resolve().then() is used instead of new Promise() to prevent
	// uncatched exception popping up in devtools
	return Promise.resolve().then(function() {
		throw new Error("Cannot find module '" + req + "'.");
	});
}
webpackEmptyAsyncContext.keys = function() { return []; };
webpackEmptyAsyncContext.resolve = webpackEmptyAsyncContext;
module.exports = webpackEmptyAsyncContext;
webpackEmptyAsyncContext.id = "./src/$$_lazy_route_resource lazy recursive";

/***/ }),

/***/ "./src/app/app.component.css":
/***/ (function(module, exports) {

module.exports = ""

/***/ }),

/***/ "./src/app/app.component.html":
/***/ (function(module, exports) {

module.exports = "<router-outlet></router-outlet>\n"

/***/ }),

/***/ "./src/app/app.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return AppComponent; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__services_user_service__ = __webpack_require__("./src/app/services/user.service.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_router__ = __webpack_require__("./node_modules/@angular/router/esm5/router.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__services_auth_service__ = __webpack_require__("./src/app/services/auth.service.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__angular_core__ = __webpack_require__("./node_modules/@angular/core/esm5/core.js");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};




var AppComponent = (function () {
    function AppComponent(userService, auth, router) {
        this.userService = userService;
        this.auth = auth;
        this.router = router;
        auth.afAuth.authState.subscribe(function (user) {
            if (!user) {
                return;
            }
            userService.save(user);
            var returnUrl = localStorage.getItem('returnUrl');
            if (!returnUrl) {
                return;
            }
            localStorage.removeItem('returnUrl');
            router.navigateByUrl(returnUrl);
        });
    }
    AppComponent = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_3__angular_core__["Component"])({
            selector: 'app-root',
            template: __webpack_require__("./src/app/app.component.html"),
            styles: [__webpack_require__("./src/app/app.component.css")]
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_0__services_user_service__["a" /* UserService */],
            __WEBPACK_IMPORTED_MODULE_2__services_auth_service__["a" /* AuthService */],
            __WEBPACK_IMPORTED_MODULE_1__angular_router__["b" /* Router */]])
    ], AppComponent);
    return AppComponent;
}());



/***/ }),

/***/ "./src/app/app.module.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return AppModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__services_links_service__ = __webpack_require__("./src/app/services/links.service.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_forms__ = __webpack_require__("./node_modules/@angular/forms/esm5/forms.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__services_filter_service__ = __webpack_require__("./src/app/services/filter.service.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__guards_admin_auth_guard__ = __webpack_require__("./src/app/guards/admin-auth.guard.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__services_user_service__ = __webpack_require__("./src/app/services/user.service.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__guards_auth_guard__ = __webpack_require__("./src/app/guards/auth-guard.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__services_auth_service__ = __webpack_require__("./src/app/services/auth.service.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__services_navbar_service__ = __webpack_require__("./src/app/services/navbar.service.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_8__angular_router__ = __webpack_require__("./node_modules/@angular/router/esm5/router.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_9__environments_environment__ = __webpack_require__("./src/environments/environment.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_10__angular_platform_browser__ = __webpack_require__("./node_modules/@angular/platform-browser/esm5/platform-browser.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_11__angular_core__ = __webpack_require__("./node_modules/@angular/core/esm5/core.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_12_angularfire2__ = __webpack_require__("./node_modules/angularfire2/index.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_13_angularfire2_database__ = __webpack_require__("./node_modules/angularfire2/database/index.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_14_angularfire2_auth__ = __webpack_require__("./node_modules/angularfire2/auth/index.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_15__ng_bootstrap_ng_bootstrap__ = __webpack_require__("./node_modules/@ng-bootstrap/ng-bootstrap/index.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_16_ng2_validation__ = __webpack_require__("./node_modules/ng2-validation/dist/index.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_16_ng2_validation___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_16_ng2_validation__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_17__app_component__ = __webpack_require__("./src/app/app.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_18__navbar_app_navbar_component__ = __webpack_require__("./src/app/navbar/app-navbar.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_19__links_links_component__ = __webpack_require__("./src/app/links/links.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_20__login_login_component__ = __webpack_require__("./src/app/login/login.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_21__links_links_filter_filter_component__ = __webpack_require__("./src/app/links/links-filter/filter.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_22__links_link_card_link_card_component__ = __webpack_require__("./src/app/links/link-card/link-card.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_23__links_links_page_links_page_component__ = __webpack_require__("./src/app/links/links-page/links-page.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_24_angularfire2_storage__ = __webpack_require__("./node_modules/angularfire2/storage/index.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_25__home_home_component__ = __webpack_require__("./src/app/home/home.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_26__services_shared_service__ = __webpack_require__("./src/app/services/shared.service.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_27__services_links_page_service__ = __webpack_require__("./src/app/services/links_page.service.ts");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};




























var AppModule = (function () {
    function AppModule() {
    }
    AppModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_11__angular_core__["NgModule"])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_17__app_component__["a" /* AppComponent */],
                __WEBPACK_IMPORTED_MODULE_18__navbar_app_navbar_component__["a" /* AppNavbarComponent */],
                __WEBPACK_IMPORTED_MODULE_19__links_links_component__["a" /* LinksComponent */],
                __WEBPACK_IMPORTED_MODULE_20__login_login_component__["a" /* LoginComponent */],
                __WEBPACK_IMPORTED_MODULE_21__links_links_filter_filter_component__["a" /* FilterComponent */],
                __WEBPACK_IMPORTED_MODULE_22__links_link_card_link_card_component__["a" /* LinkCardComponent */],
                __WEBPACK_IMPORTED_MODULE_23__links_links_page_links_page_component__["a" /* LinksPageComponent */],
                __WEBPACK_IMPORTED_MODULE_25__home_home_component__["a" /* HomeComponent */]
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_10__angular_platform_browser__["a" /* BrowserModule */],
                __WEBPACK_IMPORTED_MODULE_12_angularfire2__["a" /* AngularFireModule */].initializeApp(__WEBPACK_IMPORTED_MODULE_9__environments_environment__["a" /* environment */].firebase),
                __WEBPACK_IMPORTED_MODULE_13_angularfire2_database__["b" /* AngularFireDatabaseModule */],
                __WEBPACK_IMPORTED_MODULE_24_angularfire2_storage__["b" /* AngularFireStorageModule */],
                __WEBPACK_IMPORTED_MODULE_14_angularfire2_auth__["b" /* AngularFireAuthModule */],
                __WEBPACK_IMPORTED_MODULE_1__angular_forms__["FormsModule"],
                __WEBPACK_IMPORTED_MODULE_16_ng2_validation__["CustomFormsModule"],
                __WEBPACK_IMPORTED_MODULE_15__ng_bootstrap_ng_bootstrap__["a" /* NgbModule */].forRoot(),
                __WEBPACK_IMPORTED_MODULE_8__angular_router__["c" /* RouterModule */].forRoot([
                    { path: '', component: __WEBPACK_IMPORTED_MODULE_25__home_home_component__["a" /* HomeComponent */], pathMatch: 'full' },
                    { path: 'v/:versionStripped', component: __WEBPACK_IMPORTED_MODULE_25__home_home_component__["a" /* HomeComponent */],
                        children: [
                            { path: '', component: __WEBPACK_IMPORTED_MODULE_19__links_links_component__["a" /* LinksComponent */], pathMatch: 'full' },
                            { path: 'i', redirectTo: '', pathMatch: 'full' },
                            { path: 'i/:id', component: __WEBPACK_IMPORTED_MODULE_23__links_links_page_links_page_component__["a" /* LinksPageComponent */] }
                        ]
                    }
                ])
            ],
            providers: [
                __WEBPACK_IMPORTED_MODULE_6__services_auth_service__["a" /* AuthService */],
                __WEBPACK_IMPORTED_MODULE_5__guards_auth_guard__["a" /* AuthGuard */],
                __WEBPACK_IMPORTED_MODULE_4__services_user_service__["a" /* UserService */],
                __WEBPACK_IMPORTED_MODULE_3__guards_admin_auth_guard__["a" /* AdminAuthGuard */],
                __WEBPACK_IMPORTED_MODULE_2__services_filter_service__["a" /* FilterService */],
                __WEBPACK_IMPORTED_MODULE_0__services_links_service__["a" /* LinksService */],
                __WEBPACK_IMPORTED_MODULE_7__services_navbar_service__["a" /* NavbarService */],
                __WEBPACK_IMPORTED_MODULE_26__services_shared_service__["a" /* SharedService */],
                __WEBPACK_IMPORTED_MODULE_27__services_links_page_service__["a" /* LinksPageService */]
            ],
            bootstrap: [__WEBPACK_IMPORTED_MODULE_17__app_component__["a" /* AppComponent */]]
        })
    ], AppModule);
    return AppModule;
}());



/***/ }),

/***/ "./src/app/guards/admin-auth.guard.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return AdminAuthGuard; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__services_auth_service__ = __webpack_require__("./src/app/services/auth.service.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_core__ = __webpack_require__("./node_modules/@angular/core/esm5/core.js");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};


var AdminAuthGuard = (function () {
    function AdminAuthGuard(auth) {
        this.auth = auth;
    }
    AdminAuthGuard.prototype.canActivate = function () {
        return this.auth.afAuth.authState.map(function (user) {
            if (user.email === 'matthew.wallt@gmail.com') {
                return true;
            }
            else {
                return false;
            }
        });
    };
    AdminAuthGuard = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_1__angular_core__["Injectable"])(),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_0__services_auth_service__["a" /* AuthService */]])
    ], AdminAuthGuard);
    return AdminAuthGuard;
}());



/***/ }),

/***/ "./src/app/guards/auth-guard.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return AuthGuard; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__services_auth_service__ = __webpack_require__("./src/app/services/auth.service.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_core__ = __webpack_require__("./node_modules/@angular/core/esm5/core.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__angular_router__ = __webpack_require__("./node_modules/@angular/router/esm5/router.js");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};



var AuthGuard = (function () {
    function AuthGuard(auth, router) {
        this.auth = auth;
        this.router = router;
    }
    AuthGuard.prototype.canActivate = function (route, state) {
        var _this = this;
        return this.auth.afAuth.authState.map(function (user) {
            if (user) {
                return true;
            }
            else {
                _this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
                return false;
            }
        });
    };
    AuthGuard = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_1__angular_core__["Injectable"])(),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_0__services_auth_service__["a" /* AuthService */],
            __WEBPACK_IMPORTED_MODULE_2__angular_router__["b" /* Router */]])
    ], AuthGuard);
    return AuthGuard;
}());



/***/ }),

/***/ "./src/app/home/home.component.css":
/***/ (function(module, exports) {

module.exports = ""

/***/ }),

/***/ "./src/app/home/home.component.html":
/***/ (function(module, exports) {

module.exports = "<app-navbar [(version)]=\"version\" [versionStripped]=\"versionStripped\" [versions]=\"versions\" (searchTermChange)=\"passToLinks($event)\" ></app-navbar>\n<router-outlet></router-outlet>"

/***/ }),

/***/ "./src/app/home/home.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return HomeComponent; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("./node_modules/@angular/core/esm5/core.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_router__ = __webpack_require__("./node_modules/@angular/router/esm5/router.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__services_navbar_service__ = __webpack_require__("./src/app/services/navbar.service.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__services_shared_service__ = __webpack_require__("./src/app/services/shared.service.ts");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};




var HomeComponent = (function () {
    function HomeComponent(route, navbarService, router, sharedService) {
        this.route = route;
        this.navbarService = navbarService;
        this.router = router;
        this.sharedService = sharedService;
        this.versions = [];
    }
    HomeComponent_1 = HomeComponent;
    HomeComponent.stripChars = function (input) {
        return input.replace(/[^0-9a-z]/gi, '').toString();
    };
    HomeComponent.prototype.passToLinks = function ($event) {
        this.sharedService.getSearch($event);
    };
    HomeComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.navbarService.getAllVersions().snapshotChanges().subscribe(function (v) {
            v.forEach(function (element) {
                _this.versions.push(element.payload.val());
            });
            if (_this.version == null) {
                _this.version = _this.versions[0];
                _this.versionStripped = HomeComponent_1.stripChars(_this.version);
                _this.router.navigate(['/v/' + _this.versionStripped + '/']);
            }
        });
        this.route.paramMap.subscribe(function (params) {
            _this.version = params.get('versionStripped');
            _this.versionStripped = HomeComponent_1.stripChars(_this.version);
        });
    };
    HomeComponent = HomeComponent_1 = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'app-home',
            template: __webpack_require__("./src/app/home/home.component.html"),
            styles: [__webpack_require__("./src/app/home/home.component.css")]
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1__angular_router__["a" /* ActivatedRoute */],
            __WEBPACK_IMPORTED_MODULE_2__services_navbar_service__["a" /* NavbarService */],
            __WEBPACK_IMPORTED_MODULE_1__angular_router__["b" /* Router */],
            __WEBPACK_IMPORTED_MODULE_3__services_shared_service__["a" /* SharedService */]])
    ], HomeComponent);
    return HomeComponent;
    var HomeComponent_1;
}());



/***/ }),

/***/ "./src/app/links/link-card/link-card.component.css":
/***/ (function(module, exports) {

module.exports = ".dot {\n    height: 15px;\n    width: 15px;\n    border-radius: 50%;\n    font-size: 1vw;\n}\n\n.dot--paired {\n    background: #5FAD56;\n}\n\n.dot--deadlock {\n    background: #706C61;\n}\n\n.dot--partial {\n    background:  #F2C14E;\n}\n\n.dot--unpaired {\n    background: #A72608;\n}\n\n.light-card {\n    height: 20vw;\n    width: 20vw;\n}\n\n.light-card > .card-img-top {\n    max-height: 250px;\n    max-width: 250px;\n    height: 15vw;\n    width: 20vw;\n}\n\n.card-title {\n    font-size: 1.5vw;\n}\n\n@media screen and (max-width: 800px) {\n    #card-data {\n        display: none !important;\n    }\n}"

/***/ }),

/***/ "./src/app/links/link-card/link-card.component.html":
/***/ (function(module, exports) {

module.exports = "<a [routerLink]=\"['/v/' + _version + '/i/' + _linkObject.instance_id]\" style=\"text-decoration: none; color: #000;\">\n    <div class=\"light-card\" style=\"cursor: pointer; background: whitesmoke; border: lightgrey; border-style:solid; border-radius: 2.5%;\">\n        <img class=\"card-img-top\" src=\"{{_linkObject.mpg}}\" style=\"max-height: 250px;margin-left:-2.5px;object-fit: fill; border-bottom: lightgrey; border-radius: 2.5%; border-bottom-style: solid; background: transparent;mix-blend-mode: multiply;\">\n        <div class=\"card-body\" style=\"padding: 1.5% !important;\">\n            <div class=\"container\">\n                <div class=\"row\">\n                    <!-- locking title -->\n                    <div class=\"col-12\">\n                        <h5 class=\"card-title\" style=\"text-align: center;text-transform: uppercase;\" ngbTooltip=\"{{_linkObject.path}}\">{{_linkObject.title}}</h5>\n                    </div>\n                </div>\n                <div class=\"row\" id=\"card-data\">\n                    <!-- locking status -->\n                    <div class=\"col-3\">\n                        <p class=\"dot\" [style.background-color]=\"getStyle()\" ngbTooltip=\"{{_linkObject.status}}\"></p>\n                    </div>\n                    <!-- locking type -->\n                    <div class=\"col-3\">\n                        <p class=\"dot\" style=\"margin-left:-15px;font-style: italic;\">{{_linkObject.type}}</p>\n                    </div>\n                    <!-- locking instance id -->\n                    <div class=\"col-3\">\n                        <p class=\"dot\" style=\"text-align: center;\">{{_linkObject.instance_id}}</p>\n                    </div>\n                </div>\n            </div>\n        </div>\n    </div>\n</a>"

/***/ }),

/***/ "./src/app/links/link-card/link-card.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return LinkCardComponent; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("./node_modules/@angular/core/esm5/core.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__services_links_page_service__ = __webpack_require__("./src/app/services/links_page.service.ts");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};


var LinkCardComponent = (function () {
    function LinkCardComponent(linksPageService) {
        this.linksPageService = linksPageService;
    }
    Object.defineProperty(LinkCardComponent.prototype, "linkObject", {
        /*getter & setter for linkObject*/
        get: function () {
            return this._linkObject;
        },
        set: function (val) {
            this._linkObject = val;
            this.linksPageService.getLinkObj(this._linkObject);
        },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(LinkCardComponent.prototype, "version", {
        /*end getter and setter for linkObject*/
        /*getter & setter for version*/
        get: function () {
            return this._version;
        },
        set: function (val) {
            this._version = val;
        },
        enumerable: true,
        configurable: true
    });
    /*end getter and setter for version*/
    LinkCardComponent.prototype.getStyle = function () {
        return this.linkObject.status === 'paired' ? '#5FAD56' :
            this.linkObject.status === 'deadlock' ? '#706C61' :
                this.linkObject.status === 'partial' ? '#F2C14E' :
                    '#A72608';
    };
    LinkCardComponent.prototype.ngOnInit = function () { };
    __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Input"])('linkObject'),
        __metadata("design:type", Object),
        __metadata("design:paramtypes", [Object])
    ], LinkCardComponent.prototype, "linkObject", null);
    __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Input"])('version'),
        __metadata("design:type", String),
        __metadata("design:paramtypes", [String])
    ], LinkCardComponent.prototype, "version", null);
    LinkCardComponent = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'app-link-card',
            template: __webpack_require__("./src/app/links/link-card/link-card.component.html"),
            styles: [__webpack_require__("./src/app/links/link-card/link-card.component.css")]
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1__services_links_page_service__["a" /* LinksPageService */]])
    ], LinkCardComponent);
    return LinkCardComponent;
}());



/***/ }),

/***/ "./src/app/links/links-filter/filter.component.css":
/***/ (function(module, exports) {

module.exports = ""

/***/ }),

/***/ "./src/app/links/links-filter/filter.component.html":
/***/ (function(module, exports) {

module.exports = "<div class=\"sticky-top\" style=\"top:80px;\">\n  <div class=\"list-group\">\n    <a [class.active]=\"!driver\"\n      routerLink=\"/v/{{_version}}/\"\n      class=\"list-group-item list-group-item-action\">\n      All Drivers\n    </a>\n    <a class=\"list-group-item list-group-item-action\"\n      routerLink=\"/v/{{_version}}/\"\n      [queryParams]=\"{driver: d}\"\n      [class.active]=\"driver === d\"\n      *ngFor=\"let d of filters\">\n      {{ d }}\n    </a>\n  </div>\n</div>\n"

/***/ }),

/***/ "./src/app/links/links-filter/filter.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return FilterComponent; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__services_filter_service__ = __webpack_require__("./src/app/services/filter.service.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_core__ = __webpack_require__("./node_modules/@angular/core/esm5/core.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__angular_router__ = __webpack_require__("./node_modules/@angular/router/esm5/router.js");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : new P(function (resolve) { resolve(result.value); }).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (_) try {
            if (f = 1, y && (t = y[op[0] & 2 ? "return" : op[0] ? "throw" : "next"]) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [0, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};



var FilterComponent = (function () {
    function FilterComponent(filterService, route) {
        this.filterService = filterService;
        this.route = route;
    }
    FilterComponent_1 = FilterComponent;
    FilterComponent.stripChars = function (input) {
        return input.replace(/[^0-9a-z]/gi, '').toString();
    };
    Object.defineProperty(FilterComponent.prototype, "version", {
        get: function () {
            if (this._version !== null) {
                return FilterComponent_1.stripChars(this._version);
            }
        },
        set: function (val) {
            this._version = val;
            this.getInstanceDrivers();
        },
        enumerable: true,
        configurable: true
    });
    FilterComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.route.queryParamMap.subscribe(function (params) {
            _this.driver = params.get('driver');
        });
    };
    FilterComponent.prototype.getInstanceDrivers = function () {
        return __awaiter(this, void 0, void 0, function () {
            var _this = this;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0: return [4 /*yield*/, this.filterService.getAll(this.version).then(function (instances) {
                            _this.filters = instances.map(function (instance) {
                                return instance.driver;
                            });
                        })];
                    case 1:
                        _a.sent();
                        return [2 /*return*/];
                }
            });
        });
    };
    __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_1__angular_core__["Input"])('version'),
        __metadata("design:type", Object),
        __metadata("design:paramtypes", [Object])
    ], FilterComponent.prototype, "version", null);
    FilterComponent = FilterComponent_1 = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_1__angular_core__["Component"])({
            selector: 'app-filter',
            template: __webpack_require__("./src/app/links/links-filter/filter.component.html"),
            styles: [__webpack_require__("./src/app/links/links-filter/filter.component.css")]
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_0__services_filter_service__["a" /* FilterService */],
            __WEBPACK_IMPORTED_MODULE_2__angular_router__["a" /* ActivatedRoute */]])
    ], FilterComponent);
    return FilterComponent;
    var FilterComponent_1;
}());



/***/ }),

/***/ "./src/app/links/links-page/links-page.component.css":
/***/ (function(module, exports) {

module.exports = ".container {\n    margin-left: 232px;\n    margin-top: 15px;\n    height:100% !important;\n}"

/***/ }),

/***/ "./src/app/links/links-page/links-page.component.html":
/***/ (function(module, exports) {

module.exports = "<div class=\"container\">\n  {{link.instance_id}}\n</div>\n"

/***/ }),

/***/ "./src/app/links/links-page/links-page.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return LinksPageComponent; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("./node_modules/@angular/core/esm5/core.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__models_links__ = __webpack_require__("./src/app/models/links.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__angular_router__ = __webpack_require__("./node_modules/@angular/router/esm5/router.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__services_links_page_service__ = __webpack_require__("./src/app/services/links_page.service.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__services_links_service__ = __webpack_require__("./src/app/services/links.service.ts");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : new P(function (resolve) { resolve(result.value); }).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (_) try {
            if (f = 1, y && (t = y[op[0] & 2 ? "return" : op[0] ? "throw" : "next"]) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [0, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};





var LinksPageComponent = (function () {
    function LinksPageComponent(route, linksPageService, linksService) {
        this.route = route;
        this.linksPageService = linksPageService;
        this.linksService = linksService;
    }
    LinksPageComponent.prototype.ngOnInit = function () {
        return __awaiter(this, void 0, void 0, function () {
            var _this = this;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0: return [4 /*yield*/, this.route.parent.paramMap.subscribe(function (params) {
                            console.log('parent: ' + params.get('versionStripped'));
                            _this.version = params.get('versionStripped');
                        })];
                    case 1:
                        _a.sent();
                        return [4 /*yield*/, this.route.paramMap.subscribe(function (params) {
                                console.log('cwd: ' + params.get('id'));
                                _this.iid = params.get('id');
                            })];
                    case 2:
                        _a.sent();
                        return [4 /*yield*/, this.linksPageService.getDataFromInstanceID(this.version, this.iid).subscribe(function (data) {
                                if (data) {
                                    var link = new __WEBPACK_IMPORTED_MODULE_1__models_links__["a" /* LinksImpl */](data);
                                    _this.linksService.expandLinks(link);
                                    _this.link = link;
                                }
                            })];
                    case 3:
                        _a.sent();
                        console.log(this.link);
                        return [2 /*return*/];
                }
            });
        });
    };
    LinksPageComponent = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'app-links-page',
            template: __webpack_require__("./src/app/links/links-page/links-page.component.html"),
            styles: [__webpack_require__("./src/app/links/links-page/links-page.component.css")]
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_2__angular_router__["a" /* ActivatedRoute */],
            __WEBPACK_IMPORTED_MODULE_3__services_links_page_service__["a" /* LinksPageService */],
            __WEBPACK_IMPORTED_MODULE_4__services_links_service__["a" /* LinksService */]])
    ], LinksPageComponent);
    return LinksPageComponent;
}());



/***/ }),

/***/ "./src/app/links/links.component.css":
/***/ (function(module, exports) {

module.exports = ".container {\n    margin-left: 232px;\n    margin-top: 15px;\n    height:100% !important;\n}\n\n.flex-container {\n    margin-left: -15px !important;\n    height: 100% !important;\n    list-style: none;\n    display: -webkit-box;\n    display: -ms-flexbox;\n    display: flex;\n    -webkit-box-align: center;\n        -ms-flex-align: center;\n            align-items: center;\n    -webkit-box-pack: center;\n        -ms-flex-pack: center;\n            justify-content: center;\n}\n\n.flex-item {\n    margin-left: -15px !important;\n    text-align: center;\n}"

/***/ }),

/***/ "./src/app/links/links.component.html":
/***/ (function(module, exports) {

module.exports = "<div class=\"container\">\n    <div class=\"row\" *ngIf=\"(filteredLinks.length !== 0); else empty\">\n        <ng-container *ngFor=\"let linkObject of filteredLinks; let i = index\">\n            <div class=\"col-3\">\n                <app-link-card [linkObject]=\"linkObject\" [version]=\"_version\"></app-link-card>\n            </div>\n            <div class=\"w-100\" *ngIf=\"(i+1)%3 === 0\"></div>\n        </ng-container>\n    </div>\n    <ng-template #empty>\n        <div class=\"flex-container\">\n            <div class=\"flex-item\">\n                No Links Found. Please try a different search term.\n            </div>\n        </div>\n    </ng-template>\n</div>"

/***/ }),

/***/ "./src/app/links/links.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return LinksComponent; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_router__ = __webpack_require__("./node_modules/@angular/router/esm5/router.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__services_links_service__ = __webpack_require__("./src/app/services/links.service.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__angular_core__ = __webpack_require__("./node_modules/@angular/core/esm5/core.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__models_links__ = __webpack_require__("./src/app/models/links.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4_rxjs_add_operator_switchMap__ = __webpack_require__("./node_modules/rxjs/_esm5/add/operator/switchMap.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__services_shared_service__ = __webpack_require__("./src/app/services/shared.service.ts");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : new P(function (resolve) { resolve(result.value); }).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (_) try {
            if (f = 1, y && (t = y[op[0] & 2 ? "return" : op[0] ? "throw" : "next"]) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [0, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};






var LinksComponent = (function () {
    function LinksComponent(router, route, linksService, sharedService) {
        this.router = router;
        this.route = route;
        this.linksService = linksService;
        this.sharedService = sharedService;
        this.links = [];
    }
    LinksComponent_1 = LinksComponent;
    LinksComponent.stripChars = function (input) {
        return input.replace(/[^0-9a-z]/gi, '').toString();
    };
    Object.defineProperty(LinksComponent.prototype, "version", {
        get: function () {
            return this._version;
        },
        set: function (val) {
            this._version = val;
            this.populateLinks();
        },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(LinksComponent.prototype, "searchTerm", {
        get: function () {
            return this._searchTerm;
        },
        set: function (val) {
            this._searchTerm = val;
            // change filteredLinks based on new search term
            this.filter();
        },
        enumerable: true,
        configurable: true
    });
    LinksComponent.prototype.populateLinks = function () {
        return __awaiter(this, void 0, void 0, function () {
            var _this = this;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        // reset the data
                        this.links = [];
                        this.filteredLinks = this.links;
                        return [4 /*yield*/, this.linksService
                                .populate(LinksComponent_1.stripChars(this.version)) // this will get the version w/ no special chars
                                .subscribe(function (params) {
                                // null check
                                if (params) {
                                    Object.values(params).forEach(function (data) {
                                        var link = new __WEBPACK_IMPORTED_MODULE_3__models_links__["a" /* LinksImpl */](data);
                                        _this.linksService.expandLinks(link);
                                        _this.links.push(link);
                                    });
                                    _this.filteredLinks = _this.links;
                                }
                            })];
                    case 1:
                        _a.sent();
                        return [2 /*return*/];
                }
            });
        });
    };
    LinksComponent.prototype.filter = function () {
        var _this = this;
        this.filteredLinks = this.links.filter(function (link) {
            // check to see if it matches any of our search parameters
            return link.title.toUpperCase().includes(_this._searchTerm.toUpperCase())
                || link.type.toUpperCase().includes(_this._searchTerm.toUpperCase())
                || link.driver.toUpperCase().includes(_this._searchTerm.toUpperCase())
                || link.status.toUpperCase().includes(_this._searchTerm.toUpperCase())
                || link.filename.toUpperCase().includes(_this._searchTerm.toUpperCase())
                || link.instance_id.toUpperCase().includes(_this._searchTerm.toUpperCase());
        });
    };
    LinksComponent.prototype.ngOnInit = function () {
        var _this = this;
        // get current drivers for filtering via drivers to work
        this.route.queryParamMap.subscribe(function (params) {
            _this.driver = params.get('driver');
            _this.filteredLinks = (_this.driver) ?
                _this.links.filter(function (link) { return link.driver === _this.driver; }) :
                _this.links;
        });
        // get versionStripped in order to be able to navigate into LinksPage
        this.route.parent.paramMap.subscribe(function (params) {
            _this.version = params.get('versionStripped');
        });
        // get searchTerm from the nav-bar search bar
        this.sharedService.searchTerm.subscribe(function (term) { return _this.searchTerm = term; });
    };
    __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_2__angular_core__["Input"])('version'),
        __metadata("design:type", String),
        __metadata("design:paramtypes", [String])
    ], LinksComponent.prototype, "version", null);
    __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_2__angular_core__["Input"])('searchTerm'),
        __metadata("design:type", Object),
        __metadata("design:paramtypes", [Object])
    ], LinksComponent.prototype, "searchTerm", null);
    LinksComponent = LinksComponent_1 = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_2__angular_core__["Component"])({
            selector: 'app-links-component',
            template: __webpack_require__("./src/app/links/links.component.html"),
            styles: [__webpack_require__("./src/app/links/links.component.css")],
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_0__angular_router__["b" /* Router */],
            __WEBPACK_IMPORTED_MODULE_0__angular_router__["a" /* ActivatedRoute */],
            __WEBPACK_IMPORTED_MODULE_1__services_links_service__["a" /* LinksService */],
            __WEBPACK_IMPORTED_MODULE_5__services_shared_service__["a" /* SharedService */]])
    ], LinksComponent);
    return LinksComponent;
    var LinksComponent_1;
}());



/***/ }),

/***/ "./src/app/login/login.component.css":
/***/ (function(module, exports) {

module.exports = ""

/***/ }),

/***/ "./src/app/login/login.component.html":
/***/ (function(module, exports) {

module.exports = "<button class=\"btn btn-light\"\n        (click)=\"login()\">\n  Login with\n  <i class=\"fa text-danger fa-google-plus\"></i>\n</button>"

/***/ }),

/***/ "./src/app/login/login.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return LoginComponent; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("./node_modules/@angular/core/esm5/core.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__services_auth_service__ = __webpack_require__("./src/app/services/auth.service.ts");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};


var LoginComponent = (function () {
    function LoginComponent(auth) {
        this.auth = auth;
    }
    LoginComponent.prototype.login = function () {
        this.auth.login();
    };
    LoginComponent = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'app-login',
            template: __webpack_require__("./src/app/login/login.component.html"),
            styles: [__webpack_require__("./src/app/login/login.component.css")]
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1__services_auth_service__["a" /* AuthService */]])
    ], LoginComponent);
    return LoginComponent;
}());



/***/ }),

/***/ "./src/app/models/graph.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return GraphImpl; });
var GraphImpl = (function () {
    function GraphImpl(object) {
        var _this = this;
        this.link = [];
        Object.values(object).forEach(function (data) {
            _this.link.push(data);
        });
    }
    return GraphImpl;
}());



/***/ }),

/***/ "./src/app/models/links.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return LinksImpl; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__graph__ = __webpack_require__("./src/app/models/graph.ts");

var LinksImpl = (function () {
    function LinksImpl(object) {
        this.driver = object.driver;
        this.mpg = object.mpg;
        this.cfg = new __WEBPACK_IMPORTED_MODULE_0__graph__["a" /* GraphImpl */](object.cfg);
        this.pcg = new __WEBPACK_IMPORTED_MODULE_0__graph__["a" /* GraphImpl */](object.pcg);
        this.filename = object.filename;
        this.instance_id = object.instance_id;
        this.length = object.length;
        this.offset = object.offset;
        this.pcg = object.pcg;
        this.status = object.status;
        this.title = object.title;
        this.type = object.type;
    }
    return LinksImpl;
}());



/***/ }),

/***/ "./src/app/navbar/app-navbar.component.css":
/***/ (function(module, exports) {

module.exports = "@media (min-width: 768px) {\n    .container {\n        width: 503px;\n    }\n}\n@media (min-width: 992px) {\n    .container {\n        width: 723px;\n    }\n}\n@media (min-width: 1200px) {\n    .container {\n        width: 923px;\n    }\n}\n@media (min-width: 1432px) {\n    .container {\n        width: 1170px;\n    }\n}\nbody {\n    padding-top: 70px;\n    min-height: 200vh;\n}\n@media (min-width: 768px) {\n    body {\n        padding-top: 0;\n    }\n}\n.navbar-fixed-right,\n.navbar-fixed-left {\n    position: fixed;\n    top: 0;\n    width: 100%;\n    z-index: 1030;\n}\n@media (min-width: 768px) and (min-width: 768px) {\n    .navbar-fixed-right,\n    .navbar-fixed-left {\n        width: 232px;\n        height: 100vh;\n        border-radius: 0;\n    }\n    .navbar-fixed-right .container,\n    .navbar-fixed-left .container {\n        padding-right: 0;\n        padding-left: 0;\n        width: auto;\n    }\n    .navbar-fixed-right .navbar-header,\n    .navbar-fixed-left .navbar-header {\n        padding-left: 15px;\n        padding-right: 15px;\n    }\n    .navbar-fixed-right .navbar-collapse,\n    .navbar-fixed-left .navbar-collapse {\n        padding-right: 0;\n        padding-left: 0;\n        max-height: none;\n    }\n    .navbar-fixed-right .navbar-collapse .navbar-nav,\n    .navbar-fixed-left .navbar-collapse .navbar-nav {\n        float: none !important;\n    }\n    .navbar-fixed-right .navbar-collapse .navbar-nav > li,\n    .navbar-fixed-left .navbar-collapse .navbar-nav > li {\n        width: 100%;\n    }\n    .navbar-fixed-right .navbar-collapse .navbar-nav > li.dropdown .dropdown-menu,\n    .navbar-fixed-left .navbar-collapse .navbar-nav > li.dropdown .dropdown-menu {\n        top: 0;\n    }\n    .navbar-fixed-right .navbar-collapse .navbar-nav.navbar-right,\n    .navbar-fixed-left .navbar-collapse .navbar-nav.navbar-right {\n        margin-right: 0;\n    }\n}\n@media (min-width: 768px) {\n    body {\n        padding-left: 232px;\n    }\n}\n@media (min-width: 768px) {\n    .navbar-fixed-left {\n        right: auto !important;\n        left: 0 !important;\n        border-width: 0 1px 0 0 !important;\n    }\n    .navbar-fixed-left .dropdown .dropdown-menu {\n        left: 100%;\n        right: auto;\n        border-radius: 0 3px 3px 0;\n    }\n    .navbar-fixed-left .dropdown .dropdown-toggle .caret {\n        border-top: 4px solid transparent;\n        border-left: 4px solid;\n        border-bottom: 4px solid transparent;\n        border-right: none;\n    }\n}\n#navbar {\n    background: #f8f9fa;\n}\n.navbar-collapse.collapse {\n    display: block!important;\n}\n.navbar-nav>li, .navbar-nav {\n    float: left !important;\n}\n.navbar-nav.navbar-right:last-child {\n    margin-right: -15px !important;\n}\n.navbar-right {\n    float: right!important;\n}\n"

/***/ }),

/***/ "./src/app/navbar/app-navbar.component.html":
/***/ (function(module, exports) {

module.exports = "<nav class=\"navbar navbar-fixed-left\" id=\"navbar\">\n  <div class=\"container\">\n    <div class=\"navbar-header\" style=\"top: 2%; position: absolute;\">\n      <a class=\"navbar-brand\" routerLink=\"/v/{{versionStripped}}\">L-SAP</a>\n      <select id=\"version\" (change)=\"navigateToNewVersion($event.target.value)\" required>\n        <option *ngFor=\"let v of versions\">{{ v }}</option>\n      </select>\n    </div>\n    <div class=\"col\">\n      <div class=\"input-group mb-3 mr-0 ml-0 mt-0\">\n        <input id='search' type=\"text\" class=\"form-control\" placeholder=\"Search\" aria-label=\"Username\" aria-describedby=\"basic-addon1\" (input)=\"search($event)\"\n               ng-model=\"values\">\n      </div>\n      <app-filter [version]=\"version\"></app-filter>\n    </div>\n  </div>\n</nav>"

/***/ }),

/***/ "./src/app/navbar/app-navbar.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return AppNavbarComponent; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("./node_modules/@angular/core/esm5/core.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_router__ = __webpack_require__("./node_modules/@angular/router/esm5/router.js");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : new P(function (resolve) { resolve(result.value); }).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (_) try {
            if (f = 1, y && (t = y[op[0] & 2 ? "return" : op[0] ? "throw" : "next"]) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [0, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};


var AppNavbarComponent = (function () {
    function AppNavbarComponent(route, router) {
        this.route = route;
        this.router = router;
        this.versions = [];
        this.versionChange = new __WEBPACK_IMPORTED_MODULE_0__angular_core__["EventEmitter"]();
        this.searchTermChange = new __WEBPACK_IMPORTED_MODULE_0__angular_core__["EventEmitter"]();
    }
    AppNavbarComponent_1 = AppNavbarComponent;
    AppNavbarComponent.stripChars = function (input) {
        return input.replace(/[^0-9a-z]/gi, '').toString();
    };
    AppNavbarComponent.prototype.ngOnInit = function () { };
    AppNavbarComponent.prototype.search = function ($event) {
        return __awaiter(this, void 0, void 0, function () {
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        if (!(this.router.url === '/v/' + this._version + '/')) return [3 /*break*/, 1];
                        this.searchTerm = $event.target.value;
                        return [3 /*break*/, 3];
                    case 1: 
                    // TODO – fix this race condition. Not properly waiting for router to nav first
                    return [4 /*yield*/, this.router.navigate(['/v/' + this._version + '/']).then(this.searchTerm = $event.target.value)];
                    case 2:
                        // TODO – fix this race condition. Not properly waiting for router to nav first
                        _a.sent();
                        _a.label = 3;
                    case 3: return [2 /*return*/];
                }
            });
        });
    };
    Object.defineProperty(AppNavbarComponent.prototype, "version", {
        get: function () {
            return this._version;
        },
        set: function (val) {
            this._version = val;
            this.versionChange.emit(this.version);
        },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(AppNavbarComponent.prototype, "searchTerm", {
        get: function () {
            return this._searchTerm;
        },
        set: function (val) {
            this._searchTerm = val;
            this.searchTermChange.emit(this.searchTerm);
        },
        enumerable: true,
        configurable: true
    });
    AppNavbarComponent.prototype.navigateToNewVersion = function (version) {
        this.version = version;
        this.versionStripped = AppNavbarComponent_1.stripChars(version);
        this.router.navigate(['/v/' + this.versionStripped + '/']);
    };
    __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Input"])('versions'),
        __metadata("design:type", Array)
    ], AppNavbarComponent.prototype, "versions", void 0);
    __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Input"])('versionStripped'),
        __metadata("design:type", Object)
    ], AppNavbarComponent.prototype, "versionStripped", void 0);
    __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Output"])(),
        __metadata("design:type", Object)
    ], AppNavbarComponent.prototype, "versionChange", void 0);
    __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Output"])(),
        __metadata("design:type", Object)
    ], AppNavbarComponent.prototype, "searchTermChange", void 0);
    __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Input"])('version'),
        __metadata("design:type", String),
        __metadata("design:paramtypes", [String])
    ], AppNavbarComponent.prototype, "version", null);
    __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Input"])('searchTerm'),
        __metadata("design:type", String),
        __metadata("design:paramtypes", [String])
    ], AppNavbarComponent.prototype, "searchTerm", null);
    AppNavbarComponent = AppNavbarComponent_1 = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'app-navbar',
            template: __webpack_require__("./src/app/navbar/app-navbar.component.html"),
            styles: [__webpack_require__("./src/app/navbar/app-navbar.component.css")]
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1__angular_router__["a" /* ActivatedRoute */], __WEBPACK_IMPORTED_MODULE_1__angular_router__["b" /* Router */]])
    ], AppNavbarComponent);
    return AppNavbarComponent;
    var AppNavbarComponent_1;
}());



/***/ }),

/***/ "./src/app/services/auth.service.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return AuthService; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_router__ = __webpack_require__("./node_modules/@angular/router/esm5/router.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_angularfire2_auth__ = __webpack_require__("./node_modules/angularfire2/auth/index.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__angular_core__ = __webpack_require__("./node_modules/@angular/core/esm5/core.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3_firebase__ = __webpack_require__("./node_modules/firebase/index.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3_firebase___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_3_firebase__);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};




var AuthService = (function () {
    function AuthService(afAuth, route) {
        this.afAuth = afAuth;
        this.route = route;
    }
    AuthService.prototype.login = function () {
        var returnUrl = this.route.snapshot.queryParamMap.get('returnUrl') || '/';
        localStorage.setItem('returnUrl', returnUrl);
        this.afAuth.auth.signInWithRedirect(new __WEBPACK_IMPORTED_MODULE_3_firebase__["auth"].GoogleAuthProvider());
    };
    AuthService.prototype.logout = function () {
        this.afAuth.auth.signOut();
    };
    Object.defineProperty(AuthService.prototype, "appUser", {
        get: function () {
            return this.afAuth.authState.map(function (user) {
                if (user) {
                    return (user.email === 'matthew.wallt@gmail.com');
                }
                else {
                    return null;
                }
            });
        },
        enumerable: true,
        configurable: true
    });
    AuthService = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_2__angular_core__["Injectable"])(),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_angularfire2_auth__["a" /* AngularFireAuth */], __WEBPACK_IMPORTED_MODULE_0__angular_router__["a" /* ActivatedRoute */]])
    ], AuthService);
    return AuthService;
}());



/***/ }),

/***/ "./src/app/services/filter.service.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return FilterService; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("./node_modules/@angular/core/esm5/core.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_angularfire2_database__ = __webpack_require__("./node_modules/angularfire2/database/index.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_rxjs_operators__ = __webpack_require__("./node_modules/rxjs/_esm5/operators.js");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};



var FilterService = (function () {
    function FilterService(db) {
        this.db = db;
    }
    FilterService.prototype.getAll = function (version) {
        return this.db.list('/links/' + version + '/').valueChanges().pipe(Object(__WEBPACK_IMPORTED_MODULE_2_rxjs_operators__["b" /* first */])()).toPromise();
    };
    FilterService = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Injectable"])(),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_angularfire2_database__["a" /* AngularFireDatabase */]])
    ], FilterService);
    return FilterService;
}());



/***/ }),

/***/ "./src/app/services/links.service.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return LinksService; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("./node_modules/@angular/core/esm5/core.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_angularfire2_database__ = __webpack_require__("./node_modules/angularfire2/database/index.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_angularfire2_storage__ = __webpack_require__("./node_modules/angularfire2/storage/index.js");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : new P(function (resolve) { resolve(result.value); }).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (_) try {
            if (f = 1, y && (t = y[op[0] & 2 ? "return" : op[0] ? "throw" : "next"]) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [0, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};



var LinksService = (function () {
    function LinksService(db, storage) {
        this.db = db;
        this.storage = storage;
    }
    LinksService.prototype.populate = function (version) {
        return this.db.object('/links/' + version).valueChanges();
    };
    LinksService.prototype.getAssetURL = function (link) {
        return this.storage.ref(link).getDownloadURL();
    };
    LinksService.prototype.expandLinks = function (link) {
        return __awaiter(this, void 0, void 0, function () {
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        if (!link.mpg) return [3 /*break*/, 2];
                        return [4 /*yield*/, this.getAssetURL(link.mpg).toPromise().then(function (ref) {
                                link.mpg = ref;
                            })];
                    case 1:
                        _a.sent();
                        return [3 /*break*/, 4];
                    case 2: 
                    // placeholder image for now
                    return [4 /*yield*/, this.getAssetURL('placeholder.png').toPromise().then(function (ref) {
                            link.mpg = ref;
                        })];
                    case 3:
                        // placeholder image for now
                        _a.sent();
                        _a.label = 4;
                    case 4: return [2 /*return*/];
                }
            });
        });
    };
    LinksService = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Injectable"])(),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_angularfire2_database__["a" /* AngularFireDatabase */],
            __WEBPACK_IMPORTED_MODULE_2_angularfire2_storage__["a" /* AngularFireStorage */]])
    ], LinksService);
    return LinksService;
}());



/***/ }),

/***/ "./src/app/services/links_page.service.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return LinksPageService; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("./node_modules/@angular/core/esm5/core.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_angularfire2_database__ = __webpack_require__("./node_modules/angularfire2/database/index.js");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};


var LinksPageService = (function () {
    function LinksPageService(db) {
        this.db = db;
    }
    LinksPageService.prototype.getLinkObj = function (data) {
        this.linkObj = data;
    };
    LinksPageService.prototype.getDataFromInstanceID = function (version, iid) {
        return this.db.object('/links/' + version + '/' + iid).valueChanges();
    };
    LinksPageService = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Injectable"])(),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_angularfire2_database__["a" /* AngularFireDatabase */]])
    ], LinksPageService);
    return LinksPageService;
}());



/***/ }),

/***/ "./src/app/services/navbar.service.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return NavbarService; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("./node_modules/@angular/core/esm5/core.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_angularfire2_database__ = __webpack_require__("./node_modules/angularfire2/database/index.js");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};


var NavbarService = (function () {
    function NavbarService(db) {
        this.db = db;
    }
    NavbarService.prototype.getAllVersions = function () {
        return this.db.list('/versions');
    };
    NavbarService = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Injectable"])(),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_angularfire2_database__["a" /* AngularFireDatabase */]])
    ], NavbarService);
    return NavbarService;
}());



/***/ }),

/***/ "./src/app/services/shared.service.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return SharedService; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("./node_modules/@angular/core/esm5/core.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_rxjs_Subject__ = __webpack_require__("./node_modules/rxjs/_esm5/Subject.js");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};


var SharedService = (function () {
    function SharedService() {
        this.term = new __WEBPACK_IMPORTED_MODULE_1_rxjs_Subject__["a" /* Subject */]();
        this.searchTerm = this.term.asObservable();
    }
    SharedService.prototype.getSearch = function ($event) {
        this.term.next($event);
    };
    SharedService = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Injectable"])()
    ], SharedService);
    return SharedService;
}());



/***/ }),

/***/ "./src/app/services/user.service.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return UserService; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("./node_modules/@angular/core/esm5/core.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_angularfire2_database__ = __webpack_require__("./node_modules/angularfire2/database/index.js");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};


var UserService = (function () {
    function UserService(db) {
        this.db = db;
    }
    UserService.prototype.save = function (user) {
        this.db.object("/users/" + user.uid).update({
            name: user.displayName,
            email: user.email
        });
    };
    UserService = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Injectable"])(),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_angularfire2_database__["a" /* AngularFireDatabase */]])
    ], UserService);
    return UserService;
}());



/***/ }),

/***/ "./src/environments/environment.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return environment; });
var environment = {
    production: false,
    firebase: {
        apiKey: 'AIzaSyAcU9PvR60tAl95-Alyq-aiDKfhdIkEppU',
        authDomain: 'lsap-api.firebaseapp.com',
        databaseURL: 'https://lsap-api.firebaseio.com',
        projectId: 'lsap-api',
        storageBucket: 'lsap-api.appspot.com',
        messagingSenderId: '1002563326729'
    }
};


/***/ }),

/***/ "./src/main.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("./node_modules/@angular/core/esm5/core.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_platform_browser_dynamic__ = __webpack_require__("./node_modules/@angular/platform-browser-dynamic/esm5/platform-browser-dynamic.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__app_app_module__ = __webpack_require__("./src/app/app.module.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__environments_environment__ = __webpack_require__("./src/environments/environment.ts");




if (__WEBPACK_IMPORTED_MODULE_3__environments_environment__["a" /* environment */].production) {
    Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["enableProdMode"])();
}
Object(__WEBPACK_IMPORTED_MODULE_1__angular_platform_browser_dynamic__["a" /* platformBrowserDynamic */])().bootstrapModule(__WEBPACK_IMPORTED_MODULE_2__app_app_module__["a" /* AppModule */])
    .catch(function (err) { return console.log(err); });


/***/ }),

/***/ 0:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("./src/main.ts");


/***/ })

},[0]);
//# sourceMappingURL=main.bundle.js.map