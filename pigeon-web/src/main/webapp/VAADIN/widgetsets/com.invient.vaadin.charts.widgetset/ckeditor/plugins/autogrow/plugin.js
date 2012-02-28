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

(function(){function a(c){var d=c.getStyle('overflow-y'),e=c.getDocument(),f=CKEDITOR.dom.element.createFromHtml('<span style="margin:0;padding:0;border:0;clear:both;width:1px;height:1px;display:block;">'+(CKEDITOR.env.webkit?'&nbsp;':'')+'</span>',e);e[CKEDITOR.env.ie?'getBody':'getDocumentElement']().append(f);var g=f.getDocumentPosition(e).y+f.$.offsetHeight;f.remove();c.setStyle('overflow-y',d);return g;};var b=function(c){if(!c.window)return;var d=c.document,e=new CKEDITOR.dom.element(d.getWindow().$.frameElement),f=d.getBody(),g=d.getDocumentElement(),h=c.window.getViewPaneSize().height,i=d.$.compatMode=='BackCompat'?f:g,j=a(i);j+=c.config.autoGrow_bottomSpace||0;var k=c.config.autoGrow_minHeight!=undefined?c.config.autoGrow_minHeight:200,l=c.config.autoGrow_maxHeight||Infinity;j=Math.max(j,k);j=Math.min(j,l);if(j!=h){j=c.fire('autoGrow',{currentHeight:h,newHeight:j}).newHeight;c.resize(c.container.getStyle('width'),j,true);}if(i.$.scrollHeight>i.$.clientHeight&&j<l)i.setStyle('overflow-y','hidden');else i.removeStyle('overflow-y');};CKEDITOR.plugins.add('autogrow',{init:function(c){c.addCommand('autogrow',{exec:b,modes:{wysiwyg:1},readOnly:1,canUndo:false,editorFocus:false});var d={contentDom:1,key:1,selectionChange:1,insertElement:1};c.config.autoGrow_onStartup&&(d.instanceReady=1);for(var e in d)c.on(e,function(f){var g=c.getCommand('maximize');if(f.editor.mode=='wysiwyg'&&(!g||g.state!=CKEDITOR.TRISTATE_ON))setTimeout(function(){b(f.editor);b(f.editor);},100);});}});})();
