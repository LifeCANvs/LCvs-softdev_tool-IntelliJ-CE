// Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.diagnostic;

import com.intellij.concurrency.JobScheduler;
import com.intellij.icons.AllIcons;
import com.intellij.ui.ClickListener;
import com.intellij.ui.LayeredIcon;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author ksafonov
 */
class IdeErrorsIcon extends JLabel {
  enum State {UnreadErrors, ReadErrors, NoErrors}

  private final LayeredIcon myIcon;
  private final ActionListener myListener;
  private final boolean myEnableBlink;

  private Future myBlinker;
  private State myState;

  IdeErrorsIcon(@NotNull ActionListener listener, boolean enableBlink) {
    myListener = listener;
    myEnableBlink = enableBlink;
    setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 1));

    new ClickListener() {
      @Override
      public boolean onClick(@NotNull MouseEvent e, int clickCount) {
        if (myState != State.NoErrors) {
          myListener.actionPerformed(null);
          return true;
        }
        return false;
      }
    }.installOn(this);

    myIcon = new LayeredIcon(AllIcons.Ide.FatalError, AllIcons.Ide.FatalError_read, AllIcons.Ide.EmptyFatalError) {
      @Override
      public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
        super.paintIcon(c, g, x, y);
      }

      @Override
      public synchronized void setLayerEnabled(int layer, boolean enabled) {
        super.setLayerEnabled(layer, enabled);
      }
    };
    setIcon(myIcon);
  }

  void setState(State state) {
    myState = state;
    switch (state) {
      case UnreadErrors:
        myIcon.setLayerEnabled(0, true);
        myIcon.setLayerEnabled(1, false);
        myIcon.setLayerEnabled(2, false);
        startBlinker();
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setToolTipText(DiagnosticBundle.message("error.notification.tooltip"));
        break;

      case ReadErrors:
        stopBlinker();
        myIcon.setLayerEnabled(0, false);
        myIcon.setLayerEnabled(1, true);
        myIcon.setLayerEnabled(2, false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setToolTipText(DiagnosticBundle.message("error.notification.tooltip"));
        break;

      case NoErrors:
        // let's keep all this layers stuff for the case if we decide not to hide the icon
        stopBlinker();
        myIcon.setLayerEnabled(0, false);
        myIcon.setLayerEnabled(1, false);
        myIcon.setLayerEnabled(2, true);
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        setToolTipText(DiagnosticBundle.message("error.notification.empty.text"));
        break;

      default:
        throw new IllegalStateException(state.name());
    }
    repaint();
  }

  private synchronized void startBlinker() {
    if (myEnableBlink && myBlinker == null) {
      myBlinker = JobScheduler.getScheduler().scheduleWithFixedDelay(new Runnable() {
        boolean enabled = false;

        @Override
        public void run() {
          myIcon.setLayerEnabled(0, enabled);
          myIcon.setLayerEnabled(1, false);
          myIcon.setLayerEnabled(2, !enabled);
          repaint();
          enabled = !enabled;
        }
      }, 1, 1, TimeUnit.SECONDS);
    }
  }

  private synchronized void stopBlinker() {
    if (myBlinker != null) {
      myBlinker.cancel(true);
      myBlinker = null;
    }
  }
}