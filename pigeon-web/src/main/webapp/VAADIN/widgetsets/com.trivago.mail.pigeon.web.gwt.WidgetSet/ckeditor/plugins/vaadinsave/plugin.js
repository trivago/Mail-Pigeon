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
/*
// CKEditor for Vaadin - Widget linkage for using CKEditor within a Vaadin application.
// Copyright (C) 2010 Yozons, Inc.
//
// Adapted from CKEditor 3.2 file _source/plugins/save/plugin.js on 7 March 2010.
//
// This software is released under the Apache License 2.0 <http://www.apache.org/licenses/LICENSE-2.0.html>
//
// Used to implement the Save button in a Vaadin environment.  Basically, the button just fires the
// the 'vaadinsave' event, which we capture and if the buffer is dirty, we send it up 'immediate'.

// Original copyright
Copyright (c) 2003-2010, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

(function()
{
	var vaadinSaveCmd =
	{
		exec : function( editor )
		{
			// alert("Data: " + editor.getData());
			editor.fire( 'vaadinsave' );
		}
	};

	var pluginName = 'vaadinsave';

	// Register a plugin named "vaadinsave".
	CKEDITOR.plugins.add( pluginName,
	{
		init : function( editor )
		{
			var command = editor.addCommand( pluginName, vaadinSaveCmd );

			editor.ui.addButton( 'VaadinSave',
				{
					label : editor.lang.save,
					command : pluginName,
					icon: "../images/disk.png"
				});
		}
	});
})();
