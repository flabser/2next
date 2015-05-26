/* Modernizr 2.7.2 (Custom Build) | MIT & BSD
 * Build: http://modernizr.com/download/#-fontface-backgroundsize-borderimage-borderradius-boxshadow-flexbox-hsla-multiplebgs-opacity-rgba-textshadow-cssanimations-csscolumns-generatedcontent-cssgradients-cssreflections-csstransforms-csstransforms3d-csstransitions-applicationcache-canvas-canvastext-draganddrop-hashchange-history-audio-video-indexeddb-input-inputtypes-localstorage-postmessage-sessionstorage-websockets-websqldatabase-webworkers-geolocation-inlinesvg-smil-svg-svgclippaths-touch-webgl-shiv-cssclasses-addtest-prefixed-teststyles-testprop-testallprops-hasevent-prefixes-domprefixes-load
 */
;



window.Modernizr = (function( window, document, undefined ) {

    var version = '2.7.2',

    Modernizr = {},

    enableClasses = true,

    docElement = document.documentElement,

    mod = 'modernizr',
    modElem = document.createElement(mod),
    mStyle = modElem.style,

    inputElem  = document.createElement('input')  ,

    smile = ':)',

    toString = {}.toString,

    prefixes = ' -webkit- -moz- -o- -ms- '.split(' '),



    omPrefixes = 'Webkit Moz O ms',

    cssomPrefixes = omPrefixes.split(' '),

    domPrefixes = omPrefixes.toLowerCase().split(' '),

    ns = {'svg': 'http://www.w3.org/2000/svg'},

    tests = {},
    inputs = {},
    attrs = {},

    classes = [],

    slice = classes.slice,

    featureName, 


    injectElementWithStyles = function( rule, callback, nodes, testnames ) {

      var style, ret, node, docOverflow,
          div = document.createElement('div'),
                body = document.body,
                fakeBody = body || document.createElement('body');

      if ( parseInt(nodes, 10) ) {
                      while ( nodes-- ) {
              node = document.createElement('div');
              node.id = testnames ? testnames[nodes] : mod + (nodes + 1);
              div.appendChild(node);
          }
      }

                style = ['&#173;','<style id="s', mod, '">', rule, '</style>'].join('');
      div.id = mod;
          (body ? div : fakeBody).innerHTML += style;
      fakeBody.appendChild(div);
      if ( !body ) {
                fakeBody.style.background = '';
                fakeBody.style.overflow = 'hidden';
          docOverflow = docElement.style.overflow;
          docElement.style.overflow = 'hidden';
          docElement.appendChild(fakeBody);
      }

      ret = callback(div, rule);
        if ( !body ) {
          fakeBody.parentNode.removeChild(fakeBody);
          docElement.style.overflow = docOverflow;
      } else {
          div.parentNode.removeChild(div);
      }

      return !!ret;

    },



    isEventSupported = (function() {

      var TAGNAMES = {
        'select': 'input', 'change': 'input',
        'submit': 'form', 'reset': 'form',
        'error': 'img', 'load': 'img', 'abort': 'img'
      };

      function isEventSupported( eventName, element ) {

        element = element || document.createElement(TAGNAMES[eventName] || 'div');
        eventName = 'on' + eventName;

            var isSupported = eventName in element;

        if ( !isSupported ) {
                if ( !element.setAttribute ) {
            element = document.createElement('div');
          }
          if ( element.setAttribute && element.removeAttribute ) {
            element.setAttribute(eventName, '');
            isSupported = is(element[eventName], 'function');

                    if ( !is(element[eventName], 'undefined') ) {
              element[eventName] = undefined;
            }
            element.removeAttribute(eventName);
          }
        }

        element = null;
        return isSupported;
      }
      return isEventSupported;
    })(),


    _hasOwnProperty = ({}).hasOwnProperty, hasOwnProp;

    if ( !is(_hasOwnProperty, 'undefined') && !is(_hasOwnProperty.call, 'undefined') ) {
      hasOwnProp = function (object, property) {
        return _hasOwnProperty.call(object, property);
      };
    }
    else {
      hasOwnProp = function (object, property) { 
        return ((property in object) && is(object.constructor.prototype[property], 'undefined'));
      };
    }


    if (!Function.prototype.bind) {
      Function.prototype.bind = function bind(that) {

        var target = this;

        if (typeof target != "function") {
            throw new TypeError();
        }

        var args = slice.call(arguments, 1),
            bound = function () {

            if (this instanceof bound) {

              var F = function(){};
              F.prototype = target.prototype;
              var self = new F();

              var result = target.apply(
                  self,
                  args.concat(slice.call(arguments))
              );
              if (Object(result) === result) {
                  return result;
              }
              return self;

            } else {

              return target.apply(
                  that,
                  args.concat(slice.call(arguments))
              );

            }

        };

        return bound;
      };
    }

    function setCss( str ) {
        mStyle.cssText = str;
    }

    function setCssAll( str1, str2 ) {
        return setCss(prefixes.join(str1 + ';') + ( str2 || '' ));
    }

    function is( obj, type ) {
        return typeof obj === type;
    }

    function contains( str, substr ) {
        return !!~('' + str).indexOf(substr);
    }

    function testProps( props, prefixed ) {
        for ( var i in props ) {
            var prop = props[i];
            if ( !contains(prop, "-") && mStyle[prop] !== undefined ) {
                return prefixed == 'pfx' ? prop : true;
            }
        }
        return false;
    }

    function testDOMProps( props, obj, elem ) {
        for ( var i in props ) {
            var item = obj[props[i]];
            if ( item !== undefined) {

                            if (elem === false) return props[i];

                            if (is(item, 'function')){
                                return item.bind(elem || obj);
                }

                            return item;
            }
        }
        return false;
    }

    function testPropsAll( prop, prefixed, elem ) {

        var ucProp  = prop.charAt(0).toUpperCase() + prop.slice(1),
            props   = (prop + ' ' + cssomPrefixes.join(ucProp + ' ') + ucProp).split(' ');

            if(is(prefixed, "string") || is(prefixed, "undefined")) {
          return testProps(props, prefixed);

            } else {
          props = (prop + ' ' + (domPrefixes).join(ucProp + ' ') + ucProp).split(' ');
          return testDOMProps(props, prefixed, elem);
        }
    }    tests['flexbox'] = function() {
      return testPropsAll('flexWrap');
    };    tests['canvas'] = function() {
        var elem = document.createElement('canvas');
        return !!(elem.getContext && elem.getContext('2d'));
    };

    tests['canvastext'] = function() {
        return !!(Modernizr['canvas'] && is(document.createElement('canvas').getContext('2d').fillText, 'function'));
    };



    tests['webgl'] = function() {
        return !!window.WebGLRenderingContext;
    };


    tests['touch'] = function() {
        var bool;

        if(('ontouchstart' in window) || window.DocumentTouch && document instanceof DocumentTouch) {
          bool = true;
        } else {
          injectElementWithStyles(['@media (',prefixes.join('touch-enabled),('),mod,')','{#modernizr{top:9px;position:absolute}}'].join(''), function( node ) {
            bool = node.offsetTop === 9;
          });
        }

        return bool;
    };



    tests['geolocation'] = function() {
        return 'geolocation' in navigator;
    };


    tests['postmessage'] = function() {
      return !!window.postMessage;
    };


    tests['websqldatabase'] = function() {
      return !!window.openDatabase;
    };

    tests['indexedDB'] = function() {
      return !!testPropsAll("indexedDB", window);
    };

    tests['hashchange'] = function() {
      return isEventSupported('hashchange', window) && (document.documentMode === undefined || document.documentMode > 7);
    };

    tests['history'] = function() {
      return !!(window.history && history.pushState);
    };

    tests['draganddrop'] = function() {
        var div = document.createElement('div');
        return ('draggable' in div) || ('ondragstart' in div && 'ondrop' in div);
    };

    tests['websockets'] = function() {
        return 'WebSocket' in window || 'MozWebSocket' in window;
    };


    tests['rgba'] = function() {
        setCss('background-color:rgba(150,255,150,.5)');

        return contains(mStyle.backgroundColor, 'rgba');
    };

    tests['hsla'] = function() {
            setCss('background-color:hsla(120,40%,100%,.5)');

        return contains(mStyle.backgroundColor, 'rgba') || contains(mStyle.backgroundColor, 'hsla');
    };

    tests['multiplebgs'] = function() {
                setCss('background:url(https://),url(https://),red url(https://)');

            return (/(url\s*\(.*?){3}/).test(mStyle.background);
    };    tests['backgroundsize'] = function() {
        return testPropsAll('backgroundSize');
    };

    tests['borderimage'] = function() {
        return testPropsAll('borderImage');
    };



    tests['borderradius'] = function() {
        return testPropsAll('borderRadius');
    };

    tests['boxshadow'] = function() {
        return testPropsAll('boxShadow');
    };

    tests['textshadow'] = function() {
        return document.createElement('div').style.textShadow === '';
    };


    tests['opacity'] = function() {
                setCssAll('opacity:.55');

                    return (/^0.55$/).test(mStyle.opacity);
    };


    tests['cssanimations'] = function() {
        return testPropsAll('animationName');
    };


    tests['csscolumns'] = function() {
        return testPropsAll('columnCount');
    };


    tests['cssgradients'] = function() {
        var str1 = 'background-image:',
            str2 = 'gradient(linear,left top,right bottom,from(#9f9),to(white));',
            str3 = 'linear-gradient(left top,#9f9, white);';

        setCss(
                       (str1 + '-webkit- '.split(' ').join(str2 + str1) +
                       prefixes.join(str3 + str1)).slice(0, -str1.length)
        );

        return contains(mStyle.backgroundImage, 'gradient');
    };


    tests['cssreflections'] = function() {
        return testPropsAll('boxReflect');
    };


    tests['csstransforms'] = function() {
        return !!testPropsAll('transform');
    };


    tests['csstransforms3d'] = function() {

        var ret = !!testPropsAll('perspective');

                        if ( ret && 'webkitPerspective' in docElement.style ) {

                      injectElementWithStyles('@media (transform-3d),(-webkit-transform-3d){#modernizr{left:9px;position:absolute;height:3px;}}', function( node, rule ) {
            ret = node.offsetLeft === 9 && node.offsetHeight === 3;
          });
        }
        return ret;
    };


    tests['csstransitions'] = function() {
        return testPropsAll('transition');
    };



    tests['fontface'] = function() {
        var bool;

        injectElementWithStyles('@font-face {font-family:"font";src:url("https://")}', function( node, rule ) {
          var style = document.getElementById('smodernizr'),
              sheet = style.sheet || style.styleSheet,
              cssText = sheet ? (sheet.cssRules && sheet.cssRules[0] ? sheet.cssRules[0].cssText : sheet.cssText || '') : '';

          bool = /src/i.test(cssText) && cssText.indexOf(rule.split(' ')[0]) === 0;
        });

        return bool;
    };

    tests['generatedcontent'] = function() {
        var bool;

        injectElementWithStyles(['#',mod,'{font:0/0 a}#',mod,':after{content:"',smile,'";visibility:hidden;font:3px/1 a}'].join(''), function( node ) {
          bool = node.offsetHeight >= 3;
        });

        return bool;
    };
    tests['video'] = function() {
        var elem = document.createElement('video'),
            bool = false;

            try {
            if ( bool = !!elem.canPlayType ) {
                bool      = new Boolean(bool);
                bool.ogg  = elem.canPlayType('video/ogg; codecs="theora"')      .replace(/^no$/,'');

                            bool.h264 = elem.canPlayType('video/mp4; codecs="avc1.42E01E"') .replace(/^no$/,'');

                bool.webm = elem.canPlayType('video/webm; codecs="vp8, vorbis"').replace(/^no$/,'');
            }

        } catch(e) { }

        return bool;
    };

    tests['audio'] = function() {
        var elem = document.createElement('audio'),
            bool = false;

        try {
            if ( bool = !!elem.canPlayType ) {
                bool      = new Boolean(bool);
                bool.ogg  = elem.canPlayType('audio/ogg; codecs="vorbis"').replace(/^no$/,'');
                bool.mp3  = elem.canPlayType('audio/mpeg;')               .replace(/^no$/,'');

                                                    bool.wav  = elem.canPlayType('audio/wav; codecs="1"')     .replace(/^no$/,'');
                bool.m4a  = ( elem.canPlayType('audio/x-m4a;')            ||
                              elem.canPlayType('audio/aac;'))             .replace(/^no$/,'');
            }
        } catch(e) { }

        return bool;
    };


    tests['localstorage'] = function() {
        try {
            localStorage.setItem(mod, mod);
            localStorage.removeItem(mod);
            return true;
        } catch(e) {
            return false;
        }
    };

    tests['sessionstorage'] = function() {
        try {
            sessionStorage.setItem(mod, mod);
            sessionStorage.removeItem(mod);
            return true;
        } catch(e) {
            return false;
        }
    };


    tests['webworkers'] = function() {
        return !!window.Worker;
    };


    tests['applicationcache'] = function() {
        return !!window.applicationCache;
    };


    tests['svg'] = function() {
        return !!document.createElementNS && !!document.createElementNS(ns.svg, 'svg').createSVGRect;
    };

    tests['inlinesvg'] = function() {
      var div = document.createElement('div');
      div.innerHTML = '<svg/>';
      return (div.firstChild && div.firstChild.namespaceURI) == ns.svg;
    };

    tests['smil'] = function() {
        return !!document.createElementNS && /SVGAnimate/.test(toString.call(document.createElementNS(ns.svg, 'animate')));
    };


    tests['svgclippaths'] = function() {
        return !!document.createElementNS && /SVGClipPath/.test(toString.call(document.createElementNS(ns.svg, 'clipPath')));
    };

    function webforms() {
                                            Modernizr['input'] = (function( props ) {
            for ( var i = 0, len = props.length; i < len; i++ ) {
                attrs[ props[i] ] = !!(props[i] in inputElem);
            }
            if (attrs.list){
                                  attrs.list = !!(document.createElement('datalist') && window.HTMLDataListElement);
            }
            return attrs;
        })('autocomplete autofocus list placeholder max min multiple pattern required step'.split(' '));
                            Modernizr['inputtypes'] = (function(props) {

            for ( var i = 0, bool, inputElemType, defaultView, len = props.length; i < len; i++ ) {

                inputElem.setAttribute('type', inputElemType = props[i]);
                bool = inputElem.type !== 'text';

                                                    if ( bool ) {

                    inputElem.value         = smile;
                    inputElem.style.cssText = 'position:absolute;visibility:hidden;';

                    if ( /^range$/.test(inputElemType) && inputElem.style.WebkitAppearance !== undefined ) {

                      docElement.appendChild(inputElem);
                      defaultView = document.defaultView;

                                        bool =  defaultView.getComputedStyle &&
                              defaultView.getComputedStyle(inputElem, null).WebkitAppearance !== 'textfield' &&
                                                                                  (inputElem.offsetHeight !== 0);

                      docElement.removeChild(inputElem);

                    } else if ( /^(search|tel)$/.test(inputElemType) ){
                                                                                    } else if ( /^(url|email)$/.test(inputElemType) ) {
                                        bool = inputElem.checkValidity && inputElem.checkValidity() === false;

                    } else {
                                        bool = inputElem.value != smile;
                    }
                }

                inputs[ props[i] ] = !!bool;
            }
            return inputs;
        })('search tel url email datetime date month week time datetime-local number range color'.split(' '));
        }
    for ( var feature in tests ) {
        if ( hasOwnProp(tests, feature) ) {
                                    featureName  = feature.toLowerCase();
            Modernizr[featureName] = tests[feature]();

            classes.push((Modernizr[featureName] ? '' : 'no-') + featureName);
        }
    }

    Modernizr.input || webforms();


     Modernizr.addTest = function ( feature, test ) {
       if ( typeof feature == 'object' ) {
         for ( var key in feature ) {
           if ( hasOwnProp( feature, key ) ) {
             Modernizr.addTest( key, feature[ key ] );
           }
         }
       } else {

         feature = feature.toLowerCase();

         if ( Modernizr[feature] !== undefined ) {
                                              return Modernizr;
         }

         test = typeof test == 'function' ? test() : test;

         if (typeof enableClasses !== "undefined" && enableClasses) {
           docElement.className += ' ' + (test ? '' : 'no-') + feature;
         }
         Modernizr[feature] = test;

       }

       return Modernizr; 
     };


    setCss('');
    modElem = inputElem = null;

    ;(function(window, document) {
                var version = '3.7.0';

            var options = window.html5 || {};

            var reSkip = /^<|^(?:button|map|select|textarea|object|iframe|option|optgroup)$/i;

            var saveClones = /^(?:a|b|code|div|fieldset|h1|h2|h3|h4|h5|h6|i|label|li|ol|p|q|span|strong|style|table|tbody|td|th|tr|ul)$/i;

            var supportsHtml5Styles;

            var expando = '_html5shiv';

            var expanID = 0;

            var expandoData = {};

            var supportsUnknownElements;

        (function() {
          try {
            var a = document.createElement('a');
            a.innerHTML = '<xyz></xyz>';
                    supportsHtml5Styles = ('hidden' in a);

            supportsUnknownElements = a.childNodes.length == 1 || (function() {
                        (document.createElement)('a');
              var frag = document.createDocumentFragment();
              return (
                typeof frag.cloneNode == 'undefined' ||
                typeof frag.createDocumentFragment == 'undefined' ||
                typeof frag.createElement == 'undefined'
              );
            }());
          } catch(e) {
                    supportsHtml5Styles = true;
            supportsUnknownElements = true;
          }

        }());

            function addStyleSheet(ownerDocument, cssText) {
          var p = ownerDocument.createElement('p'),
          parent = ownerDocument.getElementsByTagName('head')[0] || ownerDocument.documentElement;

          p.innerHTML = 'x<style>' + cssText + '</style>';
          return parent.insertBefore(p.lastChild, parent.firstChild);
        }

            function getElements() {
          var elements = html5.elements;
          return typeof elements == 'string' ? elements.split(' ') : elements;
        }

            function getExpandoData(ownerDocument) {
          var data = expandoData[ownerDocument[expando]];
          if (!data) {
            data = {};
            expanID++;
            ownerDocument[expando] = expanID;
            expandoData[expanID] = data;
          }
          return data;
        }

            function createElement(nodeName, ownerDocument, data){
          if (!ownerDocument) {
            ownerDocument = document;
          }
          if(supportsUnknownElements){
            return ownerDocument.createElement(nodeName);
          }
          if (!data) {
            data = getExpandoData(ownerDocument);
          }
          var node;

          if (data.cache[nodeName]) {
            node = data.cache[nodeName].cloneNode();
          } else if (saveClones.test(nodeName)) {
            node = (data.cache[nodeName] = data.createElem(nodeName)).cloneNode();
          } else {
            node = data.createElem(nodeName);
          }

                                                    return node.canHaveChildren && !reSkip.test(nodeName) && !node.tagUrn ? data.frag.appendChild(node) : node;
        }

            function createDocumentFragment(ownerDocument, data){
          if (!ownerDocument) {
            ownerDocument = document;
          }
          if(supportsUnknownElements){
            return ownerDocument.createDocumentFragment();
          }
          data = data || getExpandoData(ownerDocument);
          var clone = data.frag.cloneNode(),
          i = 0,
          elems = getElements(),
          l = elems.length;
          for(;i<l;i++){
            clone.createElement(elems[i]);
          }
          return clone;
        }

            function shivMethods(ownerDocument, data) {
          if (!data.cache) {
            data.cache = {};
            data.createElem = ownerDocument.createElement;
            data.createFrag = ownerDocument.createDocumentFragment;
            data.frag = data.createFrag();
          }


          ownerDocument.createElement = function(nodeName) {
                    if (!html5.shivMethods) {
              return data.createElem(nodeName);
            }
            return createElement(nodeName, ownerDocument, data);
          };

          ownerDocument.createDocumentFragment = Function('h,f', 'return function(){' +
                                                          'var n=f.cloneNode(),c=n.createElement;' +
                                                          'h.shivMethods&&(' +
                                                                                                                getElements().join().replace(/[\w\-]+/g, function(nodeName) {
            data.createElem(nodeName);
            data.frag.createElement(nodeName);
            return 'c("' + nodeName + '")';
          }) +
            ');return n}'
                                                         )(html5, data.frag);
        }

            function shivDocument(ownerDocument) {
          if (!ownerDocument) {
            ownerDocument = document;
          }
          var data = getExpandoData(ownerDocument);

          if (html5.shivCSS && !supportsHtml5Styles && !data.hasCSS) {
            data.hasCSS = !!addStyleSheet(ownerDocument,
                                                                                'article,aside,dialog,figcaption,figure,footer,header,hgroup,main,nav,section{display:block}' +
                                                                                    'mark{background:#FF0;color:#000}' +
                                                                                    'template{display:none}'
                                         );
          }
          if (!supportsUnknownElements) {
            shivMethods(ownerDocument, data);
          }
          return ownerDocument;
        }

            var html5 = {

                'elements': options.elements || 'abbr article aside audio bdi canvas data datalist details dialog figcaption figure footer header hgroup main mark meter nav output progress section summary template time video',

                'version': version,

                'shivCSS': (options.shivCSS !== false),

                'supportsUnknownElements': supportsUnknownElements,

                'shivMethods': (options.shivMethods !== false),

                'type': 'default',

                'shivDocument': shivDocument,

                createElement: createElement,

                createDocumentFragment: createDocumentFragment
        };

            window.html5 = html5;

            shivDocument(document);

    }(this, document));

    Modernizr._version      = version;

    Modernizr._prefixes     = prefixes;
    Modernizr._domPrefixes  = domPrefixes;
    Modernizr._cssomPrefixes  = cssomPrefixes;


    Modernizr.hasEvent      = isEventSupported;

    Modernizr.testProp      = function(prop){
        return testProps([prop]);
    };

    Modernizr.testAllProps  = testPropsAll;


    Modernizr.testStyles    = injectElementWithStyles;
    Modernizr.prefixed      = function(prop, obj, elem){
      if(!obj) {
        return testPropsAll(prop, 'pfx');
      } else {
            return testPropsAll(prop, obj, elem);
      }
    };


    docElement.className = docElement.className.replace(/(^|\s)no-js(\s|$)/, '$1$2') +

                                                    (enableClasses ? ' js ' + classes.join(' ') : '');

    return Modernizr;

})(this, this.document);
/*yepnope1.5.4|WTFPL*/
(function(a,b,c){function d(a){return"[object Function]"==o.call(a)}function e(a){return"string"==typeof a}function f(){}function g(a){return!a||"loaded"==a||"complete"==a||"uninitialized"==a}function h(){var a=p.shift();q=1,a?a.t?m(function(){("c"==a.t?B.injectCss:B.injectJs)(a.s,0,a.a,a.x,a.e,1)},0):(a(),h()):q=0}function i(a,c,d,e,f,i,j){function k(b){if(!o&&g(l.readyState)&&(u.r=o=1,!q&&h(),l.onload=l.onreadystatechange=null,b)){"img"!=a&&m(function(){t.removeChild(l)},50);for(var d in y[c])y[c].hasOwnProperty(d)&&y[c][d].onload()}}var j=j||B.errorTimeout,l=b.createElement(a),o=0,r=0,u={t:d,s:c,e:f,a:i,x:j};1===y[c]&&(r=1,y[c]=[]),"object"==a?l.data=c:(l.src=c,l.type=a),l.width=l.height="0",l.onerror=l.onload=l.onreadystatechange=function(){k.call(this,r)},p.splice(e,0,u),"img"!=a&&(r||2===y[c]?(t.insertBefore(l,s?null:n),m(k,j)):y[c].push(l))}function j(a,b,c,d,f){return q=0,b=b||"j",e(a)?i("c"==b?v:u,a,b,this.i++,c,d,f):(p.splice(this.i++,0,a),1==p.length&&h()),this}function k(){var a=B;return a.loader={load:j,i:0},a}var l=b.documentElement,m=a.setTimeout,n=b.getElementsByTagName("script")[0],o={}.toString,p=[],q=0,r="MozAppearance"in l.style,s=r&&!!b.createRange().compareNode,t=s?l:n.parentNode,l=a.opera&&"[object Opera]"==o.call(a.opera),l=!!b.attachEvent&&!l,u=r?"object":l?"script":"img",v=l?"script":u,w=Array.isArray||function(a){return"[object Array]"==o.call(a)},x=[],y={},z={timeout:function(a,b){return b.length&&(a.timeout=b[0]),a}},A,B;B=function(a){function b(a){var a=a.split("!"),b=x.length,c=a.pop(),d=a.length,c={url:c,origUrl:c,prefixes:a},e,f,g;for(f=0;f<d;f++)g=a[f].split("="),(e=z[g.shift()])&&(c=e(c,g));for(f=0;f<b;f++)c=x[f](c);return c}function g(a,e,f,g,h){var i=b(a),j=i.autoCallback;i.url.split(".").pop().split("?").shift(),i.bypass||(e&&(e=d(e)?e:e[a]||e[g]||e[a.split("/").pop().split("?")[0]]),i.instead?i.instead(a,e,f,g,h):(y[i.url]?i.noexec=!0:y[i.url]=1,f.load(i.url,i.forceCSS||!i.forceJS&&"css"==i.url.split(".").pop().split("?").shift()?"c":c,i.noexec,i.attrs,i.timeout),(d(e)||d(j))&&f.load(function(){k(),e&&e(i.origUrl,h,g),j&&j(i.origUrl,h,g),y[i.url]=2})))}function h(a,b){function c(a,c){if(a){if(e(a))c||(j=function(){var a=[].slice.call(arguments);k.apply(this,a),l()}),g(a,j,b,0,h);else if(Object(a)===a)for(n in m=function(){var b=0,c;for(c in a)a.hasOwnProperty(c)&&b++;return b}(),a)a.hasOwnProperty(n)&&(!c&&!--m&&(d(j)?j=function(){var a=[].slice.call(arguments);k.apply(this,a),l()}:j[n]=function(a){return function(){var b=[].slice.call(arguments);a&&a.apply(this,b),l()}}(k[n])),g(a[n],j,b,n,h))}else!c&&l()}var h=!!a.test,i=a.load||a.both,j=a.callback||f,k=j,l=a.complete||f,m,n;c(h?a.yep:a.nope,!!i),i&&c(i)}var i,j,l=this.yepnope.loader;if(e(a))g(a,0,l,0);else if(w(a))for(i=0;i<a.length;i++)j=a[i],e(j)?g(j,0,l,0):w(j)?B(j):Object(j)===j&&h(j,l);else Object(a)===a&&h(a,l)},B.addPrefix=function(a,b){z[a]=b},B.addFilter=function(a){x.push(a)},B.errorTimeout=1e4,null==b.readyState&&b.addEventListener&&(b.readyState="loading",b.addEventListener("DOMContentLoaded",A=function(){b.removeEventListener("DOMContentLoaded",A,0),b.readyState="complete"},0)),a.yepnope=k(),a.yepnope.executeStack=h,a.yepnope.injectJs=function(a,c,d,e,i,j){var k=b.createElement("script"),l,o,e=e||B.errorTimeout;k.src=a;for(o in d)k.setAttribute(o,d[o]);c=j?h:c||f,k.onreadystatechange=k.onload=function(){!l&&g(k.readyState)&&(l=1,c(),k.onload=k.onreadystatechange=null)},m(function(){l||(l=1,c(1))},e),i?k.onload():n.parentNode.insertBefore(k,n)},a.yepnope.injectCss=function(a,c,d,e,g,i){var e=b.createElement("link"),j,c=i?h:c||f;e.href=a,e.rel="stylesheet",e.type="text/css";for(j in d)e.setAttribute(j,d[j]);g||(n.parentNode.insertBefore(e,n),m(c,0))}})(this,document);
Modernizr.load=function(){yepnope.apply(window,[].slice.call(arguments,0));};
;
/**
 * @author Medet
 */

'use strict';

var nb = {
	APP_NAME : location.hostname,
	LANG_ID : 'RUS',
	debug : true,
	strings : {
		'yes' : 'Да',
		'no' : 'Нет',
		ok : 'Ok',
		cancel : 'Отмена',
		select : 'Выбрать',
		dialog_select_value : 'Вы не сделали выбор'
	},
	form : {},
	dialog : {},
	utils : {},
	xhr : {}
};

var nbApp = {/* local application namespace */};

/**
 * ajax
 */
nb.ajax = function(options) {

	var deferred = $.ajax(options);

	// error
	deferred.error(function(xhr) {
		console.error('nb.ajax : error', xhr);

		if (xhr.status == 400) {
			nb.dialog.error({
				title : nb.getText('error_xhr', 'Ошибка запроса'),
				message : xhr.responseText
			});
		}

		return xhr;
	});

	return deferred;
};

/**
 * getText
 */
nb.getText = function(stringKey, defaultText, langId) {
	if (nbStrings[langId || this.LANG_ID][stringKey]) {
		return nbStrings[langId || this.LANG_ID][stringKey];
	} else {
		return (defaultText !== undefined) ? defaultText : stringKey;
	}
};

/**
 * openXML
 */
nb.openXML = function() {
	window.location.href = window.location + '&onlyxml';
};

/**
 * setValues
 */
nb.form.setValues = function(currentNode) {

	var $dlgw = $(currentNode).parents('[role=dialog]');
	var $dlgWgt = $('[data-role="nb-dialog"]', $dlgw);

	var form = nb.utils.getForm($dlgWgt[0].dialogOptions.targetForm);
	var fieldName = $dlgWgt[0].dialogOptions.fieldName;

	var nodeList; // коллекция выбранных
	var isMulti = false;
	var itemSeparate = '';
	var displaySeparate = '<br/>'; // отобразить мульти значения разделителем

	if (!form) {
		nb.dialog.warn({
			title : 'Error',
			message : 'Error nb.form.setValues > form is not found: ' + form
		});
		return false;
	}

	nodeList = $('[data-type="select"]:checked', $dlgWgt[0]);
	if (nodeList.length > 0) {
		isMulti = nodeList.length > 1;
		if (!isMulti) {
			itemSeparate = '';
		}

		return _writeValues(nodeList);
	} else {
		if ($dlgWgt[0].dialogOptions.effect) {
			$dlgw.stop();
			$dlgw.effect($dlgWgt[0].dialogOptions.effect, {
				times : 2
			}, 300);
		}

		if ($('.js-no-selected-value', $dlgw[0]).length === 0) {
			(function() {
				var $_html = $('<div class="alert alert-danger js-no-selected-value" '
						+ 'style="border-radius:2px;top:55%;left:4%;right:4%;position:absolute;">'
						+ $dlgWgt[0].dialogOptions.errorMessage + '</div>');
				$dlgWgt.after($_html);
				setTimeout(function() {
					$_html.fadeOut({
						always : function() {
							$_html.remove();
						}
					});
				}, 800);
			})();
		}

		return false;
	}

	// write values to form
	function _writeValues() {
		if (isMulti) {
			$('[name=' + fieldName + ']', form).remove();
			var htm = [];
			nodeList.each(function(index, node) {
				$('<input type="hidden" name="' + fieldName + '" value="' + node.value + '" />').appendTo(form);
				htm.push('<li>' + $(node).data('text') + '</li>');
			});
			$('#' + fieldName + 'tbl').html(htm.join(''));
		} else {
			var $fieldNode = $('[name=' + fieldName + ']', form);
			if ($fieldNode.length === 0) {
				$fieldNode = $('<input type="hidden" name="' + fieldName + '" />');
				$(form).append($fieldNode[0]);
			}

			$fieldNode.val(nodeList[0].value);
			$('#' + fieldName + 'tbl').html('<li>' + nodeList.attr('data-text') + '</li>');
		}

		return true;
	}
};

/**
 * clearField
 */
nb.utils.clearField = function(fieldName, context) {
	$('[name=' + fieldName + ']').val('');
	$('#' + fieldName + 'tbl').html('');
};

/**
 * getForm
 */
nb.utils.getForm = function(el) {
	if (el === null || el === undefined) {
		return el;
	}

	if (typeof (el) === 'string' && (document[el] && document[el].nodeName === 'FORM')) {
		return document[el];
	}

	return el.form || el;
};

/**
 * blockUI
 */
nb.utils.blockUI = function() {
	var $el = $('#nb-block-ui');
	if ($el.length === 0) {
		$el = $('<div id="nb-block-ui" style="background:rgba(0,0,0,0.1);position:fixed;top:0;left:0;bottom:0;right:0;z-index:999;"/>');
		$el.appendTo('body');
	}

	$el.css('display', 'block');
};

/**
 * unblockUI
 */
nb.utils.unblockUI = function() {
	$('#nb-block-ui').css('display', 'none');
};

/**
 * notify
 */
nb.utils.notify = function(opt) {

	var $nwrap = $('#nb-notify-wrapper');
	if (!$nwrap.length) {
		$nwrap = $('<div id="nb-notify-wrapper" class="nb-notify"></div>').appendTo('body');
	}
	var $el = $('<div class="nb-notify-entry-' + (opt.type || 'info') + '">' + opt.message + '</div>').appendTo($nwrap);

	return {
		show : function() {
			$el.css('display', 'block');
			return this;
		},
		hide : function() {
			$el.css('display', 'none');
			return this;
		},
		set : function(opt) {
			for ( var key in opt) {
				if (key === 'text') {
					$el.text(opt[key]);
				} else if (key === 'type') {
					$el.attr('class', 'nb-notify-entry-' + opt[key]);
				}
			}
			return this;
		},
		remove : function(timeout, callback) {
			if ($el === null) {
				return;
			}

			if (timeout && timeout > 0) {
				var _this = this;
				setTimeout(function() {
					$el.remove();
					$el = null;
					callback && callback();
				}, timeout);
			} else {
				$el.remove();
				$el = null;
				callback && callback();
			}
		}
	};
};

$(document).ready(function() {
	nb.LANG_ID = $.cookie('lang') || 'RUS';

	$(':checkbox').bind('click', function() {
		var $checkbox = $(this);

		if (!$checkbox.data('toggle')) {
			return true;
		}

		var name = this.name || $checkbox.data('toggle');
		var $el = $('[name=' + name + ']:checkbox:visible');

		if ($checkbox.is(':checked')) {
			$el.each(function() {
				this.checked = true;
			});
		} else {
			$el.each(function() {
				this.checked = false;
			});
		}
	});
});

/**
 * dialog
 */
nb.dialog = {
	_props : {
		title : nb.APP_NAME
	},
	info : function(opt) {
		opt.className = 'dialog-info';
		opt.width = opt.width || '360';
		opt.height = opt.height || '210';
		opt.buttons = opt.buttons || {
			'Ok' : function() {
				$(this).dialog('close');
			}
		};

		return this.show(opt);
	},
	warn : function(opt) {
		opt.className = 'dialog-warn';
		opt.width = opt.width || '360';
		opt.height = opt.height || '210';
		opt.buttons = opt.buttons || {
			'Ok' : function() {
				$(this).dialog('close');
			}
		};

		return this.show(opt);
	},
	error : function(opt) {
		opt.className = 'dialog-error';
		opt.width = opt.width || '360';
		opt.height = opt.height || '210';
		opt.buttons = opt.buttons || {
			'Ok' : function() {
				$(this).dialog('close');
			}
		};

		return this.show(opt);
	},
	execute : function(dlgInnerNode) {
		var $dlgw = $(dlgInnerNode).parents('[role=dialog]');
		var $dlgWgt = $('[data-role=nb-dialog]', $dlgw);

		$dlgWgt[0].dialogOptions.onExecute(arguments);
	},
	show : function(options) {
		var $dialog;

		options.id = options.id || null;
		options.title = options.title || this._props.title;
		options.href = options.href || null;
		options.className = options.className || '';
		options.message = options.message || null;
		options.filter = options.filter;
		options.dialogFilterListItem = options.dialogFilterListItem || 'li';
		options.buttons = options.buttons || null;
		options.dialogClass = 'nb-dialog ' + (options.dialogClass ? options.dialogClass : '');
		options.errorMessage = options.errorMessage || nb.strings.dialog_select_value;

		options.onLoad = options.onLoad || null;
		options.onExecute = options.onExecute || function() {
			if (nb.form.setValues($dialog, null)) {
				$dialog.dialog('close');
			}
		};

		options.autoOpen = true;
		if (options.modal === false) {
			options.modal = false;
		} else {
			options.modal = true;
		}
		options.width = options.width || '360';
		// options.height = options.height || '420';
		options.position = options.position || 'center';

		if (options.id === null && options.href) {
			options.id = 'dlg_' + options.href.replace(/[^a-z0-9]/gi, '');

			$dialog = $('#' + options.id);
			if ($dialog[0]) {
				if ($dialog.dialog('isOpen') === true) {
					return;
				} else {
					$dialog.dialog('open');
					return;
				}
			}
		} else if (options.id !== null) {
			$dialog = $('#' + options.id);
			if ($dialog[0]) {
				if ($dialog.dialog('isOpen') === true) {
					return;
				} else {
					$dialog.dialog('open');
					return;
				}
			}
		}

		if (options.id === null) {
			options.close = options.close || function() {
				$dialog.dialog('destroy');
				$dialog.remove();
			};
		}

		var $dlgContainer;

		if (options.href) {
			$dlgContainer = $('<div data-role="nb-dialog" id="' + options.id + '" class="nb-dialog-container '
					+ options.className + '"><div class="loading-state"></div></div>');
		} else {
			if (options.id) {
				$dlgContainer = $('<div data-role="nb-dialog" id="' + options.id + '" class="nb-dialog-container '
						+ options.className + '">' + options.message + '</div>');
			} else {
				$dlgContainer = $('<div data-role="nb-dialog" class="nb-dialog-container ' + options.className + '">'
						+ options.message + '</div>');
			}
		}

		if (options.href) {
			$dialog = $dlgContainer.load(options.href, '', function(response, status, xhr) {
				if (status === 'error') {
					$dlgContainer.html('<div class="alert alert-danger">' + status + '</div>');

					console.log('nb.dialog : load callback', xhr);
				} else {
					try {
						if (options.onLoad !== null) {
							options.onLoad(response, status, xhr);
						}
					} catch (e) {
						console.log('nb.dialog', e);
					}

					try {
						if (options.filter !== false) {
							new nb.dialog.Filter($dlgContainer, options.dialogFilterListItem, 13);
						}
					} catch (e) {
						console.log('nb.dialog', e);
					}
				}
			}).dialog(options);

			$dialog.on('click', 'a', function(e) {
				e.preventDefault();
				$dlgContainer.load(this.href);
			});

			$dialog.on('change', 'select', function(e) {
				e.preventDefault();
				$dlgContainer.load(this.href);
			});
		} else {
			$dialog = $dlgContainer.dialog(options);
		}

		$dialog[0].dialogOptions = options;

		if (nb.debug === true) {
			console.log('nb.dialog: ', options);
		}

		return $dialog;
	}
};

/**
 * nb.dialog.Filter
 */
nb.dialog.Filter = function(_containerNode, _filterNode, _initCount, _triggerLen) {

	var inputEl = null;
	var initCount = _initCount || 13;
	var triggerLen = _triggerLen || 2;
	var timeout = 300;
	var to = null;
	var enabledViewSearch = false;
	var filterNode = _filterNode || '.item';
	var $containerNode = _containerNode;
	var $dlgw = $containerNode.parents('[role=dialog]');
	var $collection;

	init();

	function init() {
		$collection = $(filterNode, $containerNode[0]);

		var isHierarchical = $('.toggle-response', $containerNode[0]).length > 0;
		if ($collection.length < initCount) {
			if (!isHierarchical) {
				return;
			}
		}

		if ($('.dialog-filter', $dlgw).length === 0) {
			$containerNode.before('<div class="dialog-filter"></div>');
		}

		$('.dialog-filter', $dlgw).append(
				'<label>Фильтр: <label><input type="text" name="keyword" data-role="search" />');

		inputEl = $('.dialog-filter input[data-role=search]', $dlgw);
		inputEl.on('keyup', function(e) {
			try {
				clearTimeout(to);
				if (e.keyCode === 13) {
					return;
				}
			} catch (ex) {
				console.log(ex);
			}

			to = setTimeout(function() {
				$collection = $(filterNode, $containerNode[0]);
				filter(e.target.value);
			}, timeout);
		});
	}

	function filter(value) {
		try {
			if (value.length >= triggerLen) {
				var hiddenCount = 0;
				$collection.attr('style', '');

				var re = new RegExp(value, 'gim');

				$collection.each(function(index, node) {
					if (!re.test(node.textContent)) {
						if ($(':checked', node).length === 0) {
							$(node).attr('style', 'display:none;');
							hiddenCount++;
						}
					}
				});

				if ($collection.length > hiddenCount) {
					inputEl.attr('title', 'By keyword [' + value + '] filtered ' + ($collection.length - hiddenCount));
				} else {
					inputEl.attr('title', 'filter_no_results');
				}
			} else {
				$collection.attr('style', '');
				inputEl.attr('title', '');
			}
		} catch (e) {
			console.log(e);
		}
	}
};

/**
 * windowOpen
 */
nb.windowOpen = function(url, id, callbacks) {
	var features, width = 800, height = 600;
	var top = (window.innerHeight - height) / 2, left = (window.innerWidth - width) / 2;
	if (top < 0) top = 0;
	if (left < 0) left = 0;
	features = 'top=' + top + ',left=' + left;
	features += ',height=' + height + ',width=' + width + ',resizable=yes,scrollbars=yes,status=no';

	var wid = 'window-' + (id || url.hashCode());

	var w = window.open('', wid, features);
	if ('about:blank' === w.document.URL || w.document.URL === '') {
		w = window.open(url, wid, features);

		if (callbacks && callbacks.onclose) {
			var timer = setInterval(function() {
				if (w.closed) {
					clearInterval(timer);
					callbacks.onclose();
				}
			}, 1000);
		}
	}
	w.focus();
};

/**
 * deleteDocument
 */
nb.xhr.deleteDocument = function(ck, typeDel) {

	if (nb.debug === true) {
		console.log('nb.xhr.deleteDocument: ', ck, typeDel);
	}

	return nb.ajax({
		type : 'POST',
		datatype : 'XML',
		url : 'Provider',
		data : {
			'type' : 'delete',
			'ck' : ck,
			'typedel' : typeDel
		}
	});
};

/**
 * restoreDeletedDocument
 */
nb.xhr.restoreDeletedDocument = function(ck) {

	if (nb.debug === true) {
		console.log('nb.xhr.restoreDeletedDocument: ', ck);
	}

	return nb.ajax({
		type : 'POST',
		datatype : 'XML',
		url : 'Provider',
		data : {
			'type' : 'undelete',
			'ck' : ck
		}
	});
};

/**
 * addDocumentToFavorite
 */
nb.xhr.addDocumentToFavorite = function(docId, docType) {

	if (nb.debug === true) {
		console.log('nb.xhr.addDocumentToFavorite: ', docId, docType);
	}

	return nb.ajax({
		type : 'POST',
		datatype : 'XML',
		url : 'Provider',
		data : {
			'type' : 'service',
			'operation' : 'add_to_favourites',
			'doctype' : docType,
			'key' : docId
		}
	})
};

/**
 * removeDocumentFromFavorite
 */
nb.xhr.removeDocumentFromFavorite = function(docId, docType) {

	if (nb.debug === true) {
		console.log('nb.xhr.removeDocumentFromFavorite: ', docId, docType);
	}

	return nb.ajax({
		type : 'POST',
		datatype : 'XML',
		url : 'Provider',
		data : {
			'type' : 'service',
			'operation' : 'remove_from_favourites',
			'doctype' : docType,
			'key' : docId
		}
	});
};

/**
 * markDocumentAsRead
 */
nb.xhr.markDocumentAsRead = function(docId, docType) {

	if (nb.debug === true) {
		console.log('nb.xhr.markDocumentAsRead: ', docId, docType);
	}

	return nb.ajax({
		type : 'POST',
		datatype : 'XML',
		url : 'Provider',
		data : {
			'type' : 'service',
			'operation' : 'mark_as_read',
			'id' : 'mark_as_read',
			'doctype' : docType,
			'key' : docId
		}
	});
};

/**
 * getUsersWichRead
 */
nb.xhr.getUsersWichRead = function(docId, docType) {

	if (nb.debug === true) {
		console.log('nb.xhr.getUsersWichRead: ', docId, docType);
	}

	return nb.ajax({
		type : 'GET',
		datatype : 'XML',
		url : 'Provider',
		data : {
			'type' : 'service',
			'operation' : 'users_which_read',
			'id' : 'users_which_read',
			'doctype' : docType,
			'key' : docId
		}
	});
};

/**
 * saveDocument
 */
nb.xhr.saveDocument = function(options) {

	options = options || {};
	var notify = nb.utils.notify({
		message : nb.getText('wait_while_document_save', 'Пожалуйста ждите... идет сохранение документа'),
		type : 'process'
	}).show();

	var xhrArgs = {
		cache : false,
		type : 'POST',
		datatype : 'XML',
		url : 'Provider',
		data : options.data || $('form').serialize(),
		beforeSend : function() {
			nb.utils.blockUI();
			$('.required, [required]', 'form').removeClass('required').removeAttr('required');
		},
		success : function(xml) {
			var jmsg = nb.utils.parseMessageToJson(xml);
			var msgText = jmsg.message[0];
			if (jmsg.status === 'ok') {
				notify.set({
					'text' : nb.getText('document_saved', 'Документ сохранен'),
					'type' : 'success'
				});
				//
				if (msgText.length > 0) {
					nb.dialog.info({
						message : msgText,
						close : function() {
							if (jmsg.redirect || options.redirect) {
								window.location.href = jmsg.redirect || options.redirect;
							}
						}
					});
				} else {
					if (jmsg.redirect || options.redirect) {
						setTimeout(function() {
							window.location.href = jmsg.redirect || options.redirect;
						}, 300);
					}
				}
			} else {
				if (msgText.indexOf('require:') === 0) {
					var fields = msgText.substr('require:'.length).split(',');
					for (i = 0; i < fields.length; i++) {
						$('#' + fields[i] + 'tbl').addClass('required');
						$('[name=' + fields[i] + ']').attr('required', 'required').addClass('required');
					}
					notify.set({
						'text' : nb.getText('required_field_not_filled', 'Не заполнены обязательные поля'),
						'type' : 'error'
					});
				} else {
					notify.set({
						'text' : msgText,
						'type' : 'error'
					});
				}
			}
		},
		error : function() {
			notify.set({
				'text' : nb.getText('error_xhr', 'Ошибка при выполнении запроса'),
				'type' : 'error'
			});
		}
	};

	var def = nb.ajax(xhrArgs);
	def.always(function() {
		nb.utils.unblockUI();
		notify.remove(2000);
	});

	return def;
};

/**
 * parseMessageToJson
 */
nb.utils.parseMessageToJson = function(xml) {

	var msg = {};
	$(xml).find('response').each(function(it) {
		msg.status = $(this).attr('status');
		msg.redirect = $('redirect', this).text();
		msg.message = [];
		$(this).find('message').each(function(it) {
			msg.message.push($(this).text());
		});
	});
	return msg;
};

/**
 * chooseFilter
 */
nb.xhr.chooseFilter = function(pageId, column, keyword) {

	if (nb.debug === true) {
		console.log('nb.xhr.chooseFilter: ', pageId, column, keyword);
	}

	return nb.ajax({
		type : 'GET',
		datatype : 'XML',
		url : 'Provider?param=filter_mode~on&param=filtered_column~' + column + '&param=key_word~' + keyword,
		cache : false,
		data : {
			'type' : 'service',
			'operation' : 'tune_session',
			'element' : 'page',
			'id' : pageId
		}
	});
};

/**
 * resetFilter
 */
nb.xhr.resetFilter = function(pageId) {

	if (nb.debug === true) {
		console.log('nb.xhr.resetFilter: ', pageId);
	}

	return nb.ajax({
		type : 'POST',
		datatype : 'XML',
		url : 'Provider',
		cache : false,
		data : {
			'type' : 'service',
			'operation' : 'tune_session',
			'element' : 'page',
			'id' : pageId,
			'param' : 'filter_mode~reset_all'
		}
	});
};

/**
 * resetCurrentFilter
 */
nb.xhr.resetCurrentFilter = function(pageId, column) {

	if (nb.debug === true) {
		console.log('nb.xhr.resetCurrentFilter: ', pageId, column);
	}

	return nb.ajax({
		type : 'GET',
		datatype : 'XML',
		url : 'Provider?param=filter_mode~on&param=filtered_column~' + column,
		cache : false,
		data : {
			'type' : 'service',
			'operation' : 'tune_session',
			'element' : 'page',
			'id' : pageId
		}
	});
};

var nbStrings = {
	'RUS' : {},
	'KAZ' : {},
	'ENG' : {},
	'CHN' : {}
};

nbStrings.RUS = {
	'yes' : 'Да',
	'no' : 'Нет',
	ok : 'Ok',
	cancel : 'Отмена',
	select : 'Выбрать',
	dialog_select_value : 'Вы не сделали выбор'
};

/**
 * sendSortRequest
 */
nb.xhr.sendSortRequest = function(pageId, column, direction) {

	if (nb.debug === true) {
		console.log('nb.xhr.sendSortRequest: ', pageId, column, direction);
	}

	return nb.ajax({
		type : 'POST',
		datatype : 'XML',
		url : 'Provider?param=sorting_mode~on&param=sorting_column~' + column.toLowerCase()
				+ '&param=sorting_direction~' + direction.toLowerCase(),
		data : {
			'type' : 'service',
			'operation' : 'tune_session',
			'element' : 'page',
			'id' : pageId
		}
	});
};

var nbApp = {
	isTouch : false,
	needDocsAction : ['delete_document'],
	allActionsNeedDocsEnabled : false
};

nbApp.init = function() {
	var md = new MobileDetect(window.navigator.userAgent);
	if (md.phone()) {
		this.uiMakeTouch('phone');
	} else if (md.tablet()) {
		this.uiMakeTouch('tablet');
	} else if (window.innerWidth <= 800) {
		this.uiMakeTouch('phone');
	}

	this.initScrollSpyActionBar();
	this.initScrollSpySide();
	this.initUI();
	this.uiToggleAvailableActions();
	$('body').removeClass('no_transition');
	$('#main-load').css('display', 'none');
};

nbApp.uiWindowResize = function() {
	if (window.innerWidth <= 800) {
		this.uiMakeTouch('phone');
	} else {
		$('body').removeClass('phone');
	}
};

/*
 * uiMakeTouch
 */
nbApp.uiMakeTouch = function(device) {
	$('body').addClass(device);
};

/*
 * uiToggleNavApp
 */
nbApp.uiToggleNavApp = function(e) {
	$('body').toggleClass('nav-app-open');
};

/*
 * uiToggleNavWorkspace
 */
nbApp.uiToggleNavWorkspace = function(e) {
	$('body').toggleClass('nav-ws-open');
};

/*
 * uiHideOpenedNav
 */
nbApp.uiHideOpenedNav = function(e) {
	$('body').removeClass('nav-app-open nav-ws-open');
};

nbApp.toggleSearchForm = function() {
	$('body').toggleClass('search-open');
};

/*
 * uiToggleAvailableActions
 */
nbApp.uiToggleAvailableActions = function(e) {
	var hasSelectedDoc = $('[name=docid]:checked').length > 0;

	if (nbApp.allActionsNeedDocsEnabled && hasSelectedDoc) {
		return;
	}

	var actCount = nbApp.needDocsAction.length;
	for (var i = 0; i < actCount; i++) {
		if (hasSelectedDoc) {
			$('.action_' + nbApp.needDocsAction[i]).removeClass('disabled').removeAttr('disabled');
			nbApp.allActionsNeedDocsEnabled = true;
		} else {
			$('.action_' + nbApp.needDocsAction[i]).addClass('disabled').attr('disabled', 'disabled');
			nbApp.allActionsNeedDocsEnabled = false;
		}
	}
};

nbApp.parseXmlMessageToJson = function(xml) {
	return nb.utils.parseMessageToJson(xml);
};

$(document).ready(function() {
	nbApp.init();
	$('#main-load').css({
		'background-color' : 'rgba(255,255,255,.5)'
	});

	window.onunload = window.onbeforeunload = function() {
		$('#main-load').show();
	};
});

nbApp.attachInitFileUpload = function(elSelector) {

	$('#progress').css('display', 'none');

	if (typeof $.fn.fileupload === 'undefined') {
		return;
	}

	var $el = $(elSelector);
	if ($el.length === 0) {
		return;
	}

	$el.fileupload({
		url : 'Uploader',
		dataType : 'xml',
		send : function() {
			$('#progress').css('display', 'block');
		},
		done : function(e, data) {
			var $tpl = $('#template-rtfcontent-entry').children().clone();
			var msg = nbApp.parseXmlMessageToJson(data.result);

			if (typeof msg.message == 'undefined' || msg.message.length === 0) {
				$('.progress-bar', $('#progress').css('display', 'none')[0]).css('width', '0%');
				alert('file upload error: empty response message');
				return;
			}

			var filename = msg.message[0].toString();
			var hash = msg.message[1].toString();
			var formsesid = msg.message[3].toString(); // upload file id
			var fileUrl = 'Provider?type=getattach&formsesid=' + formsesid;
			fileUrl += '&field=rtfcontent&id=rtfcontent&file=' + filename;

			$tpl.appendTo('#attach-files');
			$tpl.append('<input type="hidden" name="filename" value="' + filename + '"/>');
			$tpl.append('<input type="hidden" name="fileid" value="' + formsesid + '"/>');
			$tpl.append('<input type="hidden" name="filehash" value="' + hash + '"/>');
			$('a.rtf-file', $tpl).attr({
				href : fileUrl,
				target : '_blank'
			}).text(filename);
			$('[data-click=add_comment]', $tpl).on('click', function() {
				nbApp.attachAddComment(this, hash);
			});
			$('[data-click=remove]', $tpl).on('click', function() {
				nbApp.attachRemove(this, formsesid, filename, hash, true);
			});

			setTimeout(function() {
				$('.progress-bar', $('#progress').css('display', 'none')[0]).css('width', '0%');
			}, 1000);
		},
		progressall : function(e, data) {
			var progress = parseInt(data.loaded / data.total * 100, 10);
			$('#progress .progress-bar').css('width', progress + '%').text(progress + '%');
		},
		fail : function() {
			$('.progress-bar', $('#progress').css('display', 'none')[0]).css('width', '0%');
			alert('file upload fail');
		}
	}).prop('disabled', !$.support.fileInput).parent().addClass($.support.fileInput ? undefined : 'disabled');

};

nbApp.attachPreview = function(el) {
	var $rtfe = $(el).parents('.rtf-entry');
	var $link = $rtfe.find('a.rtf-file').append('<i class="rtf-loading"></i>');

	$('<img class="attach-preview" style="display:none;">').attr('src', $link.attr('href')).on('load', function() {
		$(this).show();
		$('i.rtf-loading', $link).remove();
	}).on('error', function() {
		alert('load error');
		$('i.rtf-loading', $link).remove();
		$(this).remove();
	}).appendTo($rtfe);

	$(el).hide();
};

nbApp.attachAddComment = function(el, hash) {
	var $rtfe = $(el).parents('.rtf-entry');
	var $dlg = nb.dialog.show({
		message : $('<div><textarea rows="6"></textarea></div>').html(),
		title : 'Коментарий',
		buttons : {
			'Ok' : {
				text : 'Ok',
				click : function() {
					var comment = $('textarea', $dlg).val();
					if (comment.length === 0) {
						return false;
					}

					$('#frm').append('<input type="hidden" name="comment' + hash + '" value="' + comment + '">');
					$rtfe.find('a.rtf-file').after($('<span class="rtf-comment">').html(comment));

					$(el).hide();
					$dlg.dialog('close');
				}
			},
			'Cancel' : {
				text : 'Oтмена',
				click : function() {
					$dlg.dialog('close');
				}
			}
		}
	});
};

nbApp.attachRemove = function(el, formsesid, filename, hash, deleteEntry) {
	if (deleteEntry === true) {
		$(el).parents('.rtf-entry').remove();
	} else {
		$(el).parents('.rtf-entry').addClass('to-delete');
	}
	$('#' + hash).remove();

	$('#frm').append('<input type="hidden" name="deletertfcontentsesid" value="' + formsesid + '" />');
	$('#frm').append('<input type="hidden" name="deletertfcontentname" value="' + filename + '" />');
	$('#frm').append('<input type="hidden" name="deletertfcontentfield" value="rtfcontent" />');
};

nbApp.saveDoc = function(redirectUrl) {
	var def = nb.xhr.saveDocument({
		redirect : redirectUrl
	});
	return def;
};

nbApp.closeDoc = function(redirectUrl) {
	if (typeof redirectUrl !== 'undefined' && redirectUrl.length !== 0) {
		window.location.href = redirectUrl;
	} else {
		window.history.back();
	}
};

/*
 * clearFormField
 */
nbApp.clearFormField = function(fieldName) {
	nb.utils.clearField(fieldName);
};

/*
 * markRead
 */
nbApp.markRead = function(doctype, docid) {
	setTimeout(function() {
		nb.xhr.markDocumentAsRead(docid, doctype).then(function() {
			nb.utils.notify({
				message : nb.getText('document_mark_as_read')
			}).show().remove(1500);
		});
	}, 2000);
};

/*
 * usersWhichReadInTable
 */
nbApp.usersWhichReadInTable = function(el, doctype, id) {
	nb.xhr.getUsersWichRead(id, doctype).then(function(xml) {

		var $tblNode = $('#userswhichreadtbl');
		var row_cells = ['<tr><td>', '1', '</td><td>', '3', '</td></tr>'];

		$(xml).find('entry').each(function() {
			var username = $(this).attr('username');
			if (typeof username !== 'undefined') {
				if ($('td:contains("' + username + '")', $tblNode).length === 0) {
					row_cells[1] = username;
					row_cells[3] = $(this).attr('eventtime');
					$tblNode.append(row_cells.join(''));
				}
			}
		});
	});
};

nbApp.initUI = function() {

	$('#toggle-nav-app').mousedown(function(e) {
		e.preventDefault();
		nbApp.uiToggleNavApp();
	});

	$('#toggle-nav-ws').mousedown(function(e) {
		e.preventDefault();
		nbApp.uiToggleNavWorkspace();
	});

	$('#toggle-head-search').click(function() {
		nbApp.toggleSearchForm();
	});

	$('#search-close').mousedown(function() {
		nbApp.toggleSearchForm();
	});

	if ($('#content-overlay')) {
		$('#content-overlay').mousedown(function(e) {
			e.preventDefault();
			nbApp.uiHideOpenedNav();
		});

		$('#content-overlay')[0].addEventListener('touchstart', function(e) {
			e.preventDefault();
			nbApp.uiHideOpenedNav();
		}, false);
	}

	$('[data-role="side-tree-toggle"]').click(function() {
		$(this).parent().toggleClass('side-tree-collapse');
	});

	$('[data-role="toggle"]').click(function() {
		$(this).parent().toggleClass($(this).data('toggle-class'));
	});

	// action
	$('.action_save_user_profile').click(function() {
		nbApp.saveUserProfile();
	});

	$('.action_save_and_close').click(function() {
		nbApp.saveDoc(frm.last_page.value);
	});

	$('.action_delete_document').click(function(e) {
		e.preventDefault();
		if ($(e.target).hasClass('disabled') === false) {
			nbApp.delSelectedDocument();
		}
		return false;
	});

	$('[name=docid]:checkbox').bind('change', function(e) {
		nbApp.uiToggleAvailableActions(e);
	});

	$('[data-toggle=docid]:checkbox').bind('click', function(e) {
		nbApp.uiToggleAvailableActions(e);
	});

	nbApp.attachInitFileUpload('#fileupload');

	//
	if (typeof $.fn.tabs !== 'undefined') {
		$('#tabs').tabs();
	}

	$('.js-swipe-entry').on("touchend", function(e) {
		nbApp.touch.touchEnd(e);
	});
	$('.js-swipe-entry').on("touchmove", function(e) {
		nbApp.touch.touchMove(e);
	});
	$('.js-swipe-entry').bind("touchstart", function(e) {
		nbApp.touch.touchStart(e);
	});

	$('.entries').on("touchend", function(e) {
		// nbApp.touchEnd(e);
	});
	$('.entries').on("touchmove", function(e) {
		// nbApp.touchMove(e);
	});
	$('.entries').bind("touchstart", function(e) {
		// nbApp.touchStart(e);
	});
};

nbApp.xhrSaveUserProfile = function(formNode) {
	return nb.ajax({
		type : 'POST',
		url : 'Provider?type=save&element=user_profile',
		datatype : 'html',
		data : $(formNode).serialize()
	});
};

nbApp.xhrDeleteDocument = function(docIds) {
	return nb.ajax({
		type : 'POST',
		datatype : 'XML',
		url : 'Provider?type=page&id=delete_document&nocache=' + Date.now(),
		data : docIds
	});
};

nbApp.xhrDocThreadExpand = function(id, cdoctype) {
	return nb.ajax({
		method : 'GET',
		datatype : 'html',
		url : 'Provider',
		data : {
			'type' : 'view',
			'id' : 'docthread',
			'parentdocid' : id,
			'parentdoctype' : cdoctype,
			'command' : 'expand`' + id + '`' + cdoctype
		}
	});
};

nbApp.xhrDocThreadCollapse = function(id, cdoctype) {
	return nb.ajax({
		method : 'GET',
		url : 'Provider?type=view&id=docthread&command=collaps`' + id + '`' + cdoctype
	});
};

nbApp.initScrollSpySide = function() {

	var offsetTop = 0;
	var sideOnTop = false;
	var $side = $('.nav-app');

	if ($side.length) {
		offsetTop = $('.layout_header')[0].clientHeight;

		$(window).scroll(scrollSpySide);
		scrollSpySide();
	}

	function scrollSpySide() {
		if (window.pageYOffset > offsetTop) {
			if (!sideOnTop) {
				$side.css('top', '0px');
				sideOnTop = true;
			}
		} else {
			if (sideOnTop) {
				$side.css('top', '');
				sideOnTop = false;
			}
		}
	}
};

nbApp.initScrollSpyActionBar = function() {

	var offsetTop = 0;
	var isFixed = false;
	var $bar = $('.action-bar-top');

	if ($bar.length && $bar.find('.pagination, .btn').length) {
		var $parent = $bar.parent();
		$parent.css('min-height', $parent.height() + 'px');
		offsetTop = $bar[0].clientHeight + $parent.height();
		$bar.css('width', $bar[0].clientWidth);

		$(window).scroll(scrollSpyActionBar);
		scrollSpyActionBar();
	}

	function scrollSpyActionBar() {
		if (window.pageYOffset > offsetTop) {
			if (!isFixed) {
				$bar.addClass('action-bar-fixed');
				isFixed = true;
			}
		} else {
			if (isFixed) {
				$bar.removeClass('action-bar-fixed');
				isFixed = false;
			}
		}
	}
};

nbStrings = {
	'RUS' : {
		'yes' : 'Да',
		'no' : 'Нет',
		ok : 'Ok',
		cancel : 'Отмена',
		select : 'Выбрать',
		dialog_select_value : 'Вы не сделали выбор',
		no_selected_documents : 'Не выбран документ(ы)',
		search : 'Поиск',
		comment : 'Комментарий',
		document_saved : 'Документ сохранен',
		document_mark_as_read : 'Документ отмечен как прочтенный',
		wait_while_document_save : 'Пожалуйста ждите... идет сохранение документа',
		add_comment : 'Добавить комментарий',
		attention : 'Внимание'
	},
	'KAZ' : {
		'yes' : 'Ия',
		'no' : 'Жоқ',
		ok : 'Ok',
		cancel : 'Болдырмау',
		select : 'Таңдау',
		dialog_select_value : 'Вы не сделали выбор',
		no_selected_documents : 'Не выбран документ(ы)',
		search : 'Поиск',
		comment : 'Комментарий',
		document_saved : 'Документ сохранен',
		document_mark_as_read : 'Документ отмечен как прочтенный',
		wait_while_document_save : 'Пожалуйста ждите... идет сохранение документа',
		add_comment : 'Добавить комментарий',
		attention : 'Внимание'
	},
	'ENG' : {},
	'CHN' : {}
};

var calendarStrings = {
	'RUS' : {
		monthNames : ['Январь', 'Февраль', 'Март', 'Апрель', 'Май', 'Июнь', 'Июль', 'Август', 'Сентябрь', 'Октябрь',
				'Ноябрь', 'Декабрь'],
		dayNamesMin : ['Вс', 'Пн', 'Вт', 'Ср', 'Чт', 'Пт', 'Сб']
	},
	'KAZ' : {
		monthNames : ['Қаңтар', 'Ақпан', 'Наурыз', 'Сәуір', 'Мамыр', 'Маусым', 'Шілде', 'Тамыз', 'Қыркүйек', 'Қазан',
				'Қараша', 'Желтоқсан'],
		monthNamesShort : ['Қаңтар', 'Ақпан', 'Наурыз', 'Сәуір', 'Мамыр', 'Маусым', 'Шілде', 'Тамыз', 'Қыркүйек',
				'Қазан', 'Қараша', 'Желтоқсан'],
		dayNames : ['жексебі', 'дүйсенбі', 'сейсенбі', 'сәрсенбі', 'бейсенбі', 'жұма', 'сенбі'],
		dayNamesShort : ['жек', 'дүй', 'сей', 'сәр', 'бей', 'жұм', 'сен'],
		dayNamesMin : ['Жс', 'Дс', 'Сс', 'Ср', 'Бс', 'Жм', 'Сн']
	},
	'ENG' : {
		monthNames : ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October',
				'November', 'December'],
		monthNamesShort : ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
		dayNames : ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'],
		dayNamesShort : ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
		dayNamesMin : ['Su', 'Mo', 'Tu', 'We', 'Th', 'Fr', 'Sa'],
		weekHeader : 'Wk'
	},
	'CHN' : {
		closeText : '关闭',
		prevText : '&#x3c;上月',
		nextText : '下月&#x3e;',
		currentText : '今天',
		monthNames : ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
		monthNamesShort : ['一', '二', '三', '四', '五', '六', '七', '八', '九', '十', '十一', '十二'],
		dayNames : ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'],
		dayNamesShort : ['周日', '周一', '周二', '周三', '周四', '周五', '周六'],
		dayNamesMin : ['日', '一', '二', '三', '四', '五', '六'],
		weekHeader : '周',
		yearSuffix : '年'
	}
};

;
$(function() {
	nbApp.touch = {};

	var touch = nbApp.touch;
	var t = 150;
	var i = 80;
	var n = 200;
	var s = 70;
	var a = 10;
	var o = 10;
	var r = true;
	var l = "js-swipe";

	touch.touchStart = function(touchEvent) {
		if (1 === touchEvent.originalEvent.touches.length) {
			touch.tapSwipedMessage = Boolean(touch.swipedMsg);
			touch.hideSwipedMsg();
			touch.resetValue(touchEvent);
		}
	};

	touch.touchMove = function(touchEvent) {
		var changedTouch = touchEvent.originalEvent.changedTouches[0];
		if (touch.isValidTouch(touchEvent)) {
			touch.delta = touch.x - changedTouch.pageX;
			touch.defineUserAction(changedTouch);
			if (touch.startSwipe) {
				if (!touch.startSwipeTriggered) {
					touch.startSwipeTriggered = true;
				}
				r && touch.move(touchEvent.currentTarget);
				touchEvent.preventDefault();
			}
		}
	};

	touch.touchEnd = function(touchEvent) {
		if (touch.isValidTouch(touchEvent, true) && touch.startSwipe) {
			touch.swipedMsg = touchEvent.currentTarget;
			if (touch.delta > i || new Date - touch.startTime < n) {
				// qu.animations.setStyle(touch.swipedMsg, -s, 0, t);

				touch.swipedMsg.style.cssText = '';

				var parent = $(touch.swipedMsg).parent('.entry-wrap');
				parent.addClass('entry-action-open');

				touch.swipedMsg.classList.add(l);
			} else {
				touch.hideSwipedMsg();
			}
			touchEvent.stopPropagation();
			touchEvent.preventDefault()
		}
	};

	touch.hideSwipedMsg = function() {
		if (touch.swipedMsg) {
			var i = touch.swipedMsg;
			setTimeout(function() {
				i.classList.remove(l);
				i = null
			}, t);
			// qu.animations.setStyle(touch.swipedMsg, 0, 0, t);

			touch.swipedMsg.style.cssText = '';

			var parent = $(touch.swipedMsg).parent('.entry-wrap');
			parent.removeClass('entry-action-open');

			touch.swipedMsg = null
		}
	};

	touch.resetValue = function(t) {
		var i = t.originalEvent.changedTouches[0];
		touch.touchId = i.identifier;
		touch.startTime = new Date;
		touch.startSwipe = !1;
		touch.startScroll = !1;
		touch.delta = 0;
		touch.x = i.pageX;
		touch.y = i.pageY;
		touch.startSwipeTriggered = !1
	};

	touch.bindEvents = function(t, i) {
		t["touchstart " + i] = touch.touchStart;
		t["touchend " + i] = touch.touchEnd;
		t["touchmove " + i] = touch.touchMove
	};

	touch.defineUserAction = function(t) {
		Math.abs(touch.y - t.pageY) > o && !touch.startSwipe ? touch.startScroll = !0 : touch.delta > a
				&& !touch.startScroll && (touch.startSwipe = !0)
	};

	touch.isValidTouch = function(t, i) {
		var n = i ? "changedTouches" : "targetTouches";
		return t.originalEvent[n][0].identifier === touch.touchId
	};

	touch.move = function(el) {
		var i = Math.min(-touch.delta, 0);
		-s > i && (i = -s + (s + i) / 8);
		// qu.animations.setStyle(el, i, 0, 0)

		el.style.cssText = 'transition:transform 0ms ease-in-out;-moz-transform:translate3d(' + i + 'px,0,0);-webkit-transform:translate3d(' + i + 'px,0,0);transform:translate3d(' + i + 'px,0,0)';
	};
});
;
$(function() {

	var isShow = false;
	var clickYOffset = 0;
	var click = false;
	var arrowUp = '▲';
	var arrowDown = '▼';

	var to_top = $('<div class="to_top"><div class="to_top-panel"><div class="to_top-button"></div></div></div>');

	$('body').append(to_top);

	var btn = $('.to_top-button', to_top);

	$('.to_top-panel', to_top).click(function() {
		click = true;

		if (!clickYOffset) {
			clickYOffset = window.pageYOffset;
			btn.html(arrowDown);
			$.scrollTo($('body'), 100, {
				axis : 'y'
			});
		} else {
			btn.html(arrowUp);
			$.scrollTo($('body'), 100, {
				axis : 'y',
				offset : clickYOffset
			});
			clickYOffset = 0;
		}
	});

	$(document).bind('mousewheel', function(event, delta) {
		if (click) {
			click = false;
			clickYOffset = 0;
			btn.html(arrowUp);

			if (window.pageYOffset == 0 && delta > 0) {
				show_or_hide();
			}
		} else {
			if (isShow && window.pageYOffset == 0) {
				to_top.hide();
				isShow = false;
				clickYOffset = 0;
			}
		}
	});

	$(window).scroll(show_or_hide);

	function show_or_hide() {
		if (click) {
			return;
		}

		if (window.pageYOffset > 300) {
			if (!isShow) {
				to_top.show();
				btn.html(arrowUp);
				isShow = true;
			}
		} else {
			if (isShow) {
				to_top.hide();
				btn.html(arrowDown);
				isShow = false;
			}
		}
	}

	show_or_hide();
});
nbApp.saveUserProfile = function() {

	if ($('#newpwd').val() != $('#newpwd2').val()) {
		nb.dialog.warn({
			message : 'Введенные пароли не совпадают'
		});
		return;
	}

	nbApp.xhrSaveUserProfile($('form')[0]).then(function(xml) {
		var redir = $(xml).find('redirect').text();
		if (redir === '') {
			redir = $(xml).find('history').find('entry[type=page]:last').text();
		}
		$.cookie('lang', $('select[name=lang]').val(), {
			path : '/',
			expires : 30
		});
		$.cookie('refresh', $('select[name=refresh]').val(), {
			path : '/',
			expires : 30
		});
		$.cookie('skin', $('select[name=skin]').val(), {
			expires : 30
		});
		if (redir === '') {
			window.history.back();
		} else {
			window.location = redir;
		}
	}, function(xhr, ajaxOptions, thrownError) {
		if (xhr.status === 400) {
			if (xhr.responseText.indexOf('Old password has not match') != -1) {
				nb.dialog.warn({
					message : 'Некорректно заполнено поле [пароль по умолчанию]'
				});
			} else {
				nb.dialog.error({
					message : xhr.responseText
				});
			}
		}
	});
};

/*
 * getSelectedDocumentsIDs
 */
nbApp.getSelectedDocumentsIDs = function(checkboxName) {
	var $checked = $('input[name=' + (checkboxName || 'docid') + ']:checked');
	if ($checked.length === 0) {
		return [];
	}

	var result = [];
	$checked.each(function() {
		result.push($(this).attr('id'));
	});

	return result;
};

/*
 * getSelectedDocumentsAsParam
 */
nbApp.getSelectedDocumentsAsParam = function() {
	var ids = nbApp.getSelectedDocumentsIDs();
	if (ids.length === 0) {
		return '';
	}

	var result = [];
	for ( var id in ids) {
		result.push('docid=' + ids[id]);
	}

	return result.join('&');
};

/*
 * delSelectedDocument
 */
nbApp.delSelectedDocument = function(dbID, typedel) {

	var ids;
	if (dbID) {
		ids = 'docid=' + dbID;
	} else {
		ids = nbApp.getSelectedDocumentsAsParam();
	}

	if (ids.length === 0) {
		nb.utils.notify({
			message : nb.getText('no_selected_documents', 'Не выбран документ для удаления'),
			type : 'error'
		}).show().remove(2000);
		return;
	}

	var def = nbApp.xhrDeleteDocument(ids);
	def.then(function(msg) {
		var deletedCount = $(msg).find('deleted').attr('count');
		var undeletedCount = $(msg).find('undeleted').attr('count');
		var deletedEntry = [];
		var undeletedEntry = [];

		$(msg).find('deleted').find('entry').not(':contains("undefined")').each(function() {
			deletedEntry.push('<li>' + $(this).text() + '</li>');
		});
		$(msg).find('undeleted').find('entry').not(':contains("undefined")').each(function() {
			undeletedEntry.push('<li>' + $(this).text() + '</li>');
		});

		var dlgTitle;
		if (deletedEntry.length === 0 || undeletedCount > 0) {
			dlgTitle = nb.getText('error_delete', 'Ошибка удаления');
		} else {
			dlgTitle = nb.getText('deletion_successful', 'Удаление завершено успешно');
		}

		var html = [];
		html.push('<div style=font-size:.9em;><b>Удалено:</b> ');
		html.push(deletedCount);
		html.push('<ul>');
		html.push(deletedEntry.join(''));
		html.push('</ul>');
		html.push('<b>Не удалено:</b> ');
		html.push(undeletedCount);
		html.push('<ul>');
		html.push(undeletedEntry.join(''));
		html.push('</ul></div>');

		var $dlg = nb.dialog.show({
			title : dlgTitle,
			message : html.join(''),
			buttons : {
				'Ok' : {
					text : nb.getText('ok'),
					click : function() {
						$dlg.dialog('close');
					}
				}
			},
			close : function() {
				location.reload();
			}
		});
	}, function() {
		nb.dialog.error({
			message : nb.getText('error_delete', 'Ошибка удаления')
		});
	});
};

/*
 * docAddToFav
 */
nbApp.docAddToFav = function(el, docid, doctype) {
	nb.xhr.addDocumentToFavorite(docid, doctype).then(function() {
		$(el).attr({
			'src' : '/SharedResources/img/iconset/star_full.png',
			'onclick' : 'nbApp.docRemoveFromFav(this, "' + docid + '", "' + doctype + '")'
		});
	}, function() {
		nb.dialog.error({
			message : 'Ошибка добавления в избранное'
		});
	});
};

/*
 * docRemoveFromFav
 */
nbApp.docRemoveFromFav = function(el, docid, doctype) {
	nb.xhr.removeDocumentFromFavorite(docid, doctype).then(function() {
		$(el).attr({
			'src' : '/SharedResources/img/iconset/star_empty.png',
			'onclick' : 'nbApp.docAddToFav(this, "' + docid + '", "' + doctype + '")'
		});
	}, function() {
		nb.dialog.error({
			message : 'Ошибка удаления из избранного'
		});
	});
};

/*
 * viewSortColumn
 */
nbApp.viewSortColumn = function(pageId, column, direction) {
	nb.xhr.sendSortRequest(pageId, column, direction).then(function() {
		window.location.reload();
	});
};

/*
 * viewThreadExpand
 */
nbApp.viewThreadExpand = function(id, cdoctype, pos, s) {
	nbApp.xhrDocThreadExpand(id, cdoctype).then(function(data) {
		$(data).insertAfter('.' + id);

		var href = 'javascript:nbApp.viewThreadCollapse("' + id + '", "' + cdoctype + '", "' + pos + '", "' + s + '")';
		$('#a' + id).attr('href', href);
		$('#img' + id).attr('src', '/SharedResources/img/classic/1/minus1.png');
	});
};

/*
 * viewThreadCollapse
 */
nbApp.viewThreadCollapse = function(id, cdoctype, pos, s) {
	nbApp.xhrDocThreadCollapse(id, cdoctype).then(function() {
		$('.' + id).next('tr').replaceWith('');
	});

	var href = 'javascript:nbApp.viewThreadExpand("' + id + '", "' + cdoctype + '", "' + pos + '" , "' + s + '")';
	$('#a' + id).attr('href', href);
	$('#img' + id).attr('src', '/SharedResources/img/classic/1/plus1.png');
};

/*
 * filterChoose
 */
nbApp.filterChoose = function(keyword, column) {
	nb.xhr.chooseFilter(page_id.value, column, keyword).then(function() {
		window.location.reload();
	});
};

/*
 * filterReset
 */
nbApp.filterReset = function() {
	nb.xhr.resetFilter(page_id.value).then(function() {
		window.location.reload();
	});
};

/*
 * filterResetCurrent
 */
nbApp.filterResetCurrent = function(column) {
	nb.xhr.resetCurrentFilter(page_id.value, column).then(function() {
		window.location.reload();
	});
};

function toggleFilterList(el) {
	$(el).parents('.filter-entry-list-wrapper').find('.filter-entry-list').toggleClass('visible');
}

;
nbApp.inViewEdit = function() {
	var _ed = nbApp.inViewEdit;
	var $addNewNode = $('[data-action=add_new]');
	var $layoutContent = $('.layout_content');

	_ed.contentInit = function() {
		$('[data-action=save]').on('click', function(e) {
			e.preventDefault();
		})

		$('[data-action=close]').on('click', function(e) {
			$addNewNode.removeClass('hidden');
			window.history.back(-1);
			e.preventDefault();
		});

		$('.entry-link').on('click', function(e) {
			_ed.edit(this.href);
			e.preventDefault();
		});
	};

	_ed.edit = function(url) {
		//
		if (url != window.location) {
			window.history.pushState(null, null, url);
		}
		//
		$addNewNode.addClass('hidden');

		$('#main-load').show();
		$.get(url).then(function(r) {
			$layoutContent.html(r);
			$('#main-load').hide();
			_ed.contentInit();
		});
	};

	$addNewNode.on('click', function(e) {
		_ed.edit(this.href);
		e.preventDefault();
	});

	$('a', '.side').on('click', function(e) {
		_ed.edit(this.href);
		e.preventDefault();
	});

	$(window).bind('popstate', function(e) {
		_ed.edit(location.href);
		e.preventDefault();
	});
};

$(function() {
	// nbApp.inViewEdit();
});

nbApp.dialogChoiceCategory = function(el) {
	var dlg = nb.dialog.show({
		targetForm : el.form.name,
		fieldName : "category",
		title : el.title,
		maxHeight : 500,
		minHeight : 440,
		width : 500,
		href : "Provider?type=page&id=picklist-category&page=1",
		onExecute : function() {
			if (nb.form.setValues(dlg, null)) {
				var selected = $("[data-type='select']:checked", dlg[0]);

				var type_op;
				var requireDocument;
				var requireCostCenter;

				if ($(selected[0]).hasClass("js-response")) {
					var _parent = $(selected[0]).parents(".js-parent");
					var parentCat = _parent.children("label:first");
					var parentCatName = parentCat.text();
					$("#categorytbl").html(parentCatName + " / " + $("#categorytbl").text());

					type_op = $("[name='viewtext3']", parentCat).val();
					requireDocument = $("[name='viewtext5']", parentCat).val() == "1";
					requireCostCenter = $("[name='viewtext6']", parentCat).val() == "1";
				} else {
					type_op = $("[data-id=" + selected[0].value + "][name='viewtext3']").val();
					requireDocument = $("[data-id=" + selected[0].value + "][name='viewtext5']").val() == "1";
					requireCostCenter = $("[data-id=" + selected[0].value + "][name='viewtext6']").val() == "1";
				}

				if (type_op == "in" || type_op == "out") {
					$("input[name=targetcash]").val("");
					$("#targetcashtbl").html("");
					$("#control-row-targetcash").hide();
				} else if (type_op == "transfer") {
					$("#control-row-targetcash").show();
					$("input[name=costcenter]").val("");
					$("#costcentertbl").html("");
				} else if (type_op == "calcstuff") {
					$("#control-row-targetcash").show();
					$("input[name=costcenter]").val("");
					$("#costcentertbl").html("");
				} else if (type_op == "getcash") {
					requireCostCenter = true;
				} else if (type_op == "withdraw") {

				}

				if (requireDocument) {
					$("[name=documented]").attr("required", "required").attr("checked", true);
					$("[name=documented]").attr("onclick", "return false");
					$("[name=documented]").attr("onkeydown", "return false");
				} else {
					$("[name=documented]").removeAttr("required").removeAttr("disabled");
					$("[name=documented]").attr("onclick", null);
					$("[name=documented]").attr("onkeydown", null);
				}

				if (requireCostCenter) {
					$("[name=costcenter]").attr("required", "required");
				} else {
					$("[name=costcenter]").removeAttr("required").removeAttr("disabled");
				}

				$("#subcategorytbl").html("");
				$("#typeoperationtbl").attr("class", "operation-type-icon-" + type_op).attr("title", type_op);
				$("input[name=typeoperation]").val(type_op);

				$("input[name=subcategory]").val("");

				dlg.dialog("close");
			}
		},
		buttons : {
			"ok" : {
				text : nb.getText("select"),
				click : function() {
					dlg[0].dialogOptions.onExecute();
				}
			},
			"cancel" : {
				text : nb.getText("cancel"),
				click : function() {
					dlg.dialog("close");
				}
			}
		},
		close : function() {
			$("input[name=summa]").focus();
		}
	});
};

nbApp.dialogChoiceTargetCash = function(el) {
	var dlg = nb.dialog.show({
		targetForm : el.form.name,
		fieldName : "targetcash",
		title : el.title,
		href : "Provider?type=page&id=picklist-cash&page=1",
		buttons : {
			"ok" : {
				text : nb.getText("select"),
				click : function() {
					dlg[0].dialogOptions.onExecute();
				}
			},
			"cancel" : {
				text : nb.getText("cancel"),
				click : function() {
					dlg.dialog("close");
				}
			}
		},
		close : function() {
			$("input[name=summa]").focus();
		}
	});
};

nbApp.dialogChoiceCostCenter = function(el) {
	var dlg = nb.dialog.show({
		targetForm : el.form.name,
		fieldName : "costcenter",
		title : el.title,
		href : "Provider?type=page&id=picklist-costcenter&page=1",
		buttons : {
			"ok" : {
				text : nb.getText("select"),
				click : function() {
					dlg[0].dialogOptions.onExecute();
				}
			},
			"cancel" : {
				text : nb.getText("cancel"),
				click : function() {
					dlg.dialog("close");
				}
			}
		},
		close : function() {
			$("textarea[name=basis]").focus();
		}
	});
};

nbApp.dialogChoiceBossAndDemp = function(el, fieldName, isMulti) {
	var dlg = nb.dialog.show({
		targetForm : el.form.name,
		fieldName : fieldName,
		dialogFilterListItem : ".tree-entry",
		title : el.title,
		maxHeight : 500,
		minHeight : 440,
		width : 500,
		href : "Provider?type=view&id=bossandemppicklist&page=1&fieldName=" + fieldName + "&isMulti=" + isMulti,
		onLoad : function() {
			if (isMulti === false) {
				$("[type='checkbox'][data-type='select']", dlg[0]).attr("type", "radio");
			}
		},
		buttons : {
			"select" : {
				text : nb.getText("select"),
				click : function() {
					dlg[0].dialogOptions.onExecute();
				}
			},
			"cancel" : {
				text : nb.getText("cancel"),
				click : function() {
					dlg.dialog("close");
				}
			}
		}
	});
};

nbApp.dialogChoiceAccessRoles = function(el) {
	var dlg = nb.dialog.show({
		targetForm : el.form.name,
		fieldName : "accessroles",
		title : el.title,
		href : "Provider?type=view&id=picklist-roles&page=1",
		buttons : {
			"ok" : {
				text : nb.getText("select"),
				click : function() {
					dlg[0].dialogOptions.onExecute();
				}
			},
			"cancel" : {
				text : nb.getText("cancel"),
				click : function() {
					dlg.dialog("close");
				}
			}
		}
	});
};

nbApp.dialogChoiceCategoryForFormula = function(el) {
	var dlg = nb.dialog.show({
		targetForm : el.form.name,
		fieldName : "formula",
		title : el.title,
		maxHeight : 500,
		minHeight : 440,
		width : 500,
		href : "Provider?type=page&id=picklist-category-formula&page=1",
		onExecute : function() {
			var selected = $("[data-type='select']:checked", dlg[0]);
			if (selected.length === 0) {
				return false;
			}

			var ddbid = selected.data("id");
			nb.ajax({
				url : "Provider?type=page&id=get-document-data",
				method : "GET",
				dataType : "json",
				data : {
					ddbid : ddbid,
					items : "comment"
				}
			}).then(function(result) {
				$("[name='formula']").val("#" + ddbid + "@" + result.comment + "#");
				dlg.dialog("close");
			});
		},
		buttons : {
			"ok" : {
				text : nb.getText("select"),
				click : function() {
					dlg[0].dialogOptions.onExecute();
				}
			},
			"cancel" : {
				text : nb.getText("cancel"),
				click : function() {
					dlg.dialog("close");
				}
			}
		},
		close : function() {
			$("[name=formula]").focus();
		}
	});
};

nbApp.sendInvite = function(email) {
	nbApp.xhrSendInvite(email).then(function(res) {
		console.log(res);
	});
};

nbApp.oauth = function(providerID) {

	var config = {
		"vk" : {
			providerID : "vk",
			authorization : "https://oauth.vk.com/authorize",
			client_id : "4832372",
			response_type : "token",
			state : "",
			scope : "email",
			redirect_uri : "http://localhost:7777/CashTracker/Provider?type=page&id=alloperations"
		}
	};

	return new JSO(config[providerID]);
};

nbApp.xhrGetSaldoOnDate = function(ddbid) {
	return nb.ajax({
		method : "GET",
		url : "Provider?type=page&id=saldo_on_date&ddbid=" + ddbid
	});
};

nbApp.xhrGetSaldoSum = function() {
	return nb.ajax({
		method : "GET",
		url : "Provider?type=page&id=saldo-sum"
	});
};

nbApp.xhrSendInvite = function(email) {
	return nb.ajax({
		cache : false,
		method : "POST",
		url : "Provider?type=page&id=send-invite&invite_to=" + email + "&nocache=" + Date.now()
	});
};

nbApp.xhrGetCostCenterJson = function() {
	return nb.ajax({
		method : "GET",
		datatype : 'JSON',
		url : "Provider?type=page&id=costcenter-json"
	});
};
nbApp.appendSaldoToElTitleByDocDate = function(el, ddbid) {
	nbApp.xhrGetSaldoOnDate(ddbid).then(function(result) {
		$(el).attr('title', result);
	});
};

$(document).ready(function() {
	$('.js_saldo_on_date', '.view').on('mouseover', function() {
		nbApp.appendSaldoToElTitleByDocDate(this, $(this).data('ddbid'));
		$(this).off('mouseover');
	});

	$('.action-delete', '.view').on('click', function(e) {
		nbApp.delSelectedDocument($(this).data('ddbid'));
		e.preventDefault();
	});
});

nbApp.expandCollapseCategory = function(el, id, cdoctype) {
	// js-entrywrap{@id}

	if (el.classList.contains('minus')) {
		nb.ajax({
			method : 'GET',
			datatype : 'html',
			url : 'Provider',
			data : {
				'type' : 'page',
				'id' : 'category-child',
				'parentdocid' : id,
				'parentdoctype' : cdoctype,
				'command' : 'expand`' + id + '`' + cdoctype
			}
		}).then(function() {
			console.log(this)
		});
	} else {
		nb.ajax({
			method : 'GET',
			url : 'Provider?type=page&id=category-child&command=collaps`' + id + '`' + cdoctype
		}).then(function() {
			console.log(this)
		});
	}
};

nbApp.wlc = {};

nbApp.wlc.init = function() {

	var $regForm = $('form[name=form-reg]');
	$regForm.submit(function(e) {
		e.preventDefault();
		nbApp.wlc.reg(this);
	});
	$('input', $regForm).blur(function() {
		if ($(this).attr('required')) {
			if ($(this).val()) {
				$(this).removeClass('invalid');
			}
		}
	});
	$('input', $regForm).focus(function() {
		$(this).removeClass('invalid');
		$(".reg-email-invalid,.reg-email-exists,.reg-pwd-weak").css("height", "0px");
	});
	//
	$('.js-ShowLoginForm').click(nbApp.wlc.loginFormOpen);

	if (location.hash === "#sign-in") {
		nbApp.wlc.loginFormOpen();
	}
};

nbApp.wlc.loginFormOpen = function() {
	$("body").addClass("login-form-open");
};

nbApp.wlc.loginFormClose = function() {
	$("body").removeClass("login-form-open");
};

nbApp.wlc.login = function(form) {
	form.submit();
};

nbApp.wlc.setLang = function(lang) {
	$.cookie('lang', lang);
	window.location.reload();
};

nbApp.wlc.reg = function(form) {
	if ($(form).hasClass("process")) {
		return false;
	}

	$('input', form).removeClass('invalid');
	$(".reg-email-invalid,.reg-email-exists,.reg-pwd-weak", form).css("height", "0px");
	$(form).addClass("process");

	nb.ajax({
		method : 'POST',
		datatype : 'text',
		url : 'Provider?client=' + screen.height + 'x' + screen.width,
		data : $(form).serialize(),
		success : function(result) {
			var pr = result.split(",");
			if (pr.indexOf("email") != -1) {
				$("input[name=email]", form).addClass("invalid");
				$(".reg-email-invalid").css("height", "auto");
			}
			if (pr.indexOf("user-exists") != -1) {
				$(".reg-email-exists", form).css("height", "auto");
			}
			if (pr.indexOf("pwd-weak") != -1) {
				$("input[name=pwd]", form).addClass("invalid");
				$(".reg-pwd-weak", form).css("height", "auto");
			}
			//
			var isReg = false;
			if (pr.indexOf("user-reg") != -1) {
				console.log("user-reg");
				isReg = true;
			}
			if (pr.indexOf("ok") != -1) {
				console.log("ok");
			}
			//
			if (false && isReg) {
				var $loginForm = $('form[name=login-form]');
				$("[name=login]", $loginForm).val($("input[name=email]", form).val());
				$("[name=pwd]", $loginForm).val($("input[name=pwd]", form).val());
				nbApp.wlc.login($loginForm[0]);
			}

			if (pr.indexOf("verify-email-send") != -1) {
				nb.utils.notify({
					type : "info",
					message : "Для завершения регистрации подтвердите свой email"
				}).show();
			}
		},
		error : function(err) {
			console.log(err);
		},
		complete : function() {
			$(form).removeClass("process");
		}
	});
};
