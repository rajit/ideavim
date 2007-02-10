package com.maddyhome.idea.vim.action;

/*
 * IdeaVim - A Vim emulator plugin for IntelliJ Idea
 * Copyright (C) 2003-2005 Rick Maddy
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.maddyhome.idea.vim.KeyHandler;
import com.maddyhome.idea.vim.VimPlugin;
import com.maddyhome.idea.vim.helper.DataPackage;

import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;

public class PassThruDelegateAction extends AbstractDelegateAction
{
    public PassThruDelegateAction(KeyStroke stroke)
    {
        this.stroke = stroke;
    }

    public void actionPerformed(AnActionEvent event)
    {
        logger.debug("actionPerformed key=" + stroke);
        final Editor editor = (Editor)event.getDataContext().getData(DataConstants.EDITOR); // API change - don't merge
        if (editor == null || !VimPlugin.isEnabled())
        {
            getOrigAction().actionPerformed(event);
        }
        else if (event.getInputEvent() instanceof KeyEvent)
        {
            KeyStroke key = KeyStroke.getKeyStrokeForEvent((KeyEvent)event.getInputEvent());
            logger.debug("event = KeyEvent: " + key);
            KeyHandler.getInstance().handleKey(editor, key, new DataPackage(event));
        }
        else
        {
            logger.debug("event is a " + event.getInputEvent().getClass().getName());
            KeyHandler.getInstance().handleKey(editor, stroke, new DataPackage(event));
        }
    }

    private KeyStroke stroke;

    private static Logger logger = Logger.getInstance(PassThruDelegateAction.class.getName());
}