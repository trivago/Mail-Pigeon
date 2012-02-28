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

CKEDITOR.plugins.add('uicolor',{requires:['dialog'],lang:['en','he'],init:function(a){if(CKEDITOR.env.ie6Compat)return;a.addCommand('uicolor',new CKEDITOR.dialogCommand('uicolor'));a.ui.addButton('UIColor',{label:a.lang.uicolor.title,command:'uicolor',icon:this.path+'uicolor.gif'});CKEDITOR.dialog.add('uicolor',this.path+'dialogs/uicolor.js');CKEDITOR.scriptLoader.load(CKEDITOR.getUrl('plugins/uicolor/yui/yui.js'));a.element.getDocument().appendStyleSheet(CKEDITOR.getUrl('plugins/uicolor/yui/assets/yui.css'));}});
