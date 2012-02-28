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

CKEDITOR.dialog.add('button',function(a){function b(c){var e=this;var d=e.getValue();if(d){c.attributes[e.id]=d;if(e.id=='name')c.attributes['data-cke-saved-name']=d;}else{delete c.attributes[e.id];if(e.id=='name')delete c.attributes['data-cke-saved-name'];}};return{title:a.lang.button.title,minWidth:350,minHeight:150,onShow:function(){var e=this;delete e.button;var c=e.getParentEditor().getSelection().getSelectedElement();if(c&&c.is('input')){var d=c.getAttribute('type');if(d in {button:1,reset:1,submit:1}){e.button=c;e.setupContent(c);}}},onOk:function(){var c=this.getParentEditor(),d=this.button,e=!d,f=d?CKEDITOR.htmlParser.fragment.fromHtml(d.getOuterHtml()).children[0]:new CKEDITOR.htmlParser.element('input');this.commitContent(f);var g=new CKEDITOR.htmlParser.basicWriter();f.writeHtml(g);var h=CKEDITOR.dom.element.createFromHtml(g.getHtml(),c.document);if(e)c.insertElement(h);else{h.replace(d);c.getSelection().selectElement(h);}},contents:[{id:'info',label:a.lang.button.title,title:a.lang.button.title,elements:[{id:'name',type:'text',label:a.lang.common.name,'default':'',setup:function(c){this.setValue(c.data('cke-saved-name')||c.getAttribute('name')||'');},commit:b},{id:'value',type:'text',label:a.lang.button.text,accessKey:'V','default':'',setup:function(c){this.setValue(c.getAttribute('value')||'');},commit:b},{id:'type',type:'select',label:a.lang.button.type,'default':'button',accessKey:'T',items:[[a.lang.button.typeBtn,'button'],[a.lang.button.typeSbm,'submit'],[a.lang.button.typeRst,'reset']],setup:function(c){this.setValue(c.getAttribute('type')||'');},commit:b}]}]};});
