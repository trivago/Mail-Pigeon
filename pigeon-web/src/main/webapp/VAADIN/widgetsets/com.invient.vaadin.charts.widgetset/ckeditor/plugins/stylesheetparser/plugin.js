/*
 * Copyright (C) 2011-2012 trivago GmbH <mario.mueller@trivago.com>, <christian.krause@trivago.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
﻿/*
Copyright (c) 2003-2011, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

(function(){function a(c,d,e){var f=c.join(' ');f=f.replace(/(,|>|\+|~)/g,' ');f=f.replace(/\[[^\]]*/g,'');f=f.replace(/#[^\s]*/g,'');f=f.replace(/\:{1,2}[^\s]*/g,'');f=f.replace(/\s+/g,' ');var g=f.split(' '),h=[];for(var i=0;i<g.length;i++){var j=g[i];if(e.test(j)&&!d.test(j))if(CKEDITOR.tools.indexOf(h,j)==-1)h.push(j);}return h;};function b(c,d,e){var f=[],g=[],h;for(h=0;h<c.styleSheets.length;h++){var i=c.styleSheets[h],j=i.ownerNode||i.owningElement;if(j.getAttribute('data-cke-temp'))continue;if(i.href&&i.href.substr(0,9)=='chrome://')continue;var k=i.cssRules||i.rules;for(var l=0;l<k.length;l++)g.push(k[l].selectorText);}var m=a(g,d,e);for(h=0;h<m.length;h++){var n=m[h].split('.'),o=n[0].toLowerCase(),p=n[1];f.push({name:o+'.'+p,element:o,attributes:{'class':p}});}return f;};CKEDITOR.plugins.add('stylesheetparser',{requires:['styles'],onLoad:function(){var c=CKEDITOR.editor.prototype;c.getStylesSet=CKEDITOR.tools.override(c.getStylesSet,function(d){return function(e){var f=this;d.call(this,function(g){var h=f.config.stylesheetParser_skipSelectors||/(^body\.|^\.)/i,i=f.config.stylesheetParser_validSelectors||/\w+\.\w+/;e(f._.stylesDefinitions=g.concat(b(f.document.$,h,i)));});};});}});})();
