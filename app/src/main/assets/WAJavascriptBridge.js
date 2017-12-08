 (function () {
              console.log('Android');
        ApiCore = {
            __GLOBAL_FUNC_INDEX__:0,
            invokeClientMethod: function(module, name, parameters, callback) {
                var r;
                try{
                    var cbName = '';
                    if (callback) {
                        if (typeof callback == "function") {
                            cbName = ApiCore.createGlobalFuncForCallback(callback);
                        } else {
                            cbName = callback;
                        }
                    }
                    r = window.AndroidJSInterfaceV2.invoke(module,name,JSON.stringify(parameters || {}),cbName);
                }catch(e){
                    if(console) {
                        console.log(e);
                    }
                }
                return r;
            },
            createGlobalFuncForCallback: function(callback){
                if (callback) {
                    var name = '__GLOBAL_CALLBACK__' + (ApiCore.__GLOBAL_FUNC_INDEX__++);
                    window[name] = function(){
                        var args = arguments;
                        var func = (typeof callback == "function") ? callback : window[callback];
                        //we need to use setimeout here to avoid ui thread being frezzen
                        setTimeout(function(){ func.apply(null, args); }, 0);
                    };
                    return name;
                }
                return null;
            },
            invokeWebMethod: function(callback, returnValue) {
                ApiCore.invokeCallbackWithArgs(callback, [returnValue]);
            },

            invokeCallbackWithArgs: function(callback, args) {
                if (callback) {
                    var func = null;
                    var tmp;
                    if (typeof callback == "function") {
                        func = callback;
                    }
                    else if((tmp = window[callback]) && typeof tmp == 'function') {
                        func = tmp;
                    }
                    if (func) {
                        setTimeout(function(){ func.apply(null, args); }, 0);
                    }
                }
            }
        };
  }) ();
