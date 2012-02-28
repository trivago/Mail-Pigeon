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

(function(){var a=/\[\[[^\]]+\]\]/g;CKEDITOR.plugins.add('placeholder',{requires:['dialog'],lang:['en','he'],init:function(b){var c=b.lang.placeholder;b.addCommand('createplaceholder',new CKEDITOR.dialogCommand('createplaceholder'));b.addCommand('editplaceholder',new CKEDITOR.dialogCommand('editplaceholder'));b.ui.addButton('CreatePlaceholder',{label:c.toolbar,command:'createplaceholder',icon:this.path+'placeholder.gif'});if(b.addMenuItems){b.addMenuGroup('placeholder',20);b.addMenuItems({editplaceholder:{label:c.edit,command:'editplaceholder',group:'placeholder',order:1,icon:this.path+'placeholder.gif'}});if(b.contextMenu)b.contextMenu.addListener(function(d,e){if(!d||!d.data('cke-placeholder'))return null;return{editplaceholder:CKEDITOR.TRISTATE_OFF};});}b.on('doubleclick',function(d){if(CKEDITOR.plugins.placeholder.getSelectedPlaceHoder(b))d.data.dialog='editplaceholder';});b.addCss('.cke_placeholder{background-color: #ffff00;'+(CKEDITOR.env.gecko?'cursor: default;':'')+'}');b.on('contentDom',function(){b.document.getBody().on('resizestart',function(d){if(b.getSelection().getSelectedElement().data('cke-placeholder'))d.data.preventDefault();});});CKEDITOR.dialog.add('createplaceholder',this.path+'dialogs/placeholder.js');CKEDITOR.dialog.add('editplaceholder',this.path+'dialogs/placeholder.js');},afterInit:function(b){var c=b.dataProcessor,d=c&&c.dataFilter,e=c&&c.htmlFilter;if(d)d.addRules({text:function(f){return f.replace(a,function(g){return CKEDITOR.plugins.placeholder.createPlaceholder(b,null,g,1);});}});if(e)e.addRules({elements:{span:function(f){if(f.attributes&&f.attributes['data-cke-placeholder'])delete f.name;}}});}});})();CKEDITOR.plugins.placeholder={createPlaceholder:function(a,b,c,d){var e=new CKEDITOR.dom.element('span',a.document);e.setAttributes({contentEditable:'false','data-cke-placeholder':1,'class':'cke_placeholder'});c&&e.setText(c);if(d)return e.getOuterHtml();if(b){if(CKEDITOR.env.ie){e.insertAfter(b);setTimeout(function(){b.remove();e.focus();},10);}else e.replace(b);}else a.insertElement(e);return null;},getSelectedPlaceHoder:function(a){var b=a.getSelection().getRanges()[0];b.shrink(CKEDITOR.SHRINK_TEXT);var c=b.startContainer;while(c&&!(c.type==CKEDITOR.NODE_ELEMENT&&c.data('cke-placeholder')))c=c.getParent();return c;}};
