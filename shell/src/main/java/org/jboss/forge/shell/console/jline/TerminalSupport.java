/*
 * Copyright (c) 2002-2007, Marc Prud'hommeaux. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */

package org.jboss.forge.shell.console.jline;

import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

/**
 * Provides support for {@link Terminal} instances.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 2.0
 */
public abstract class TerminalSupport
         implements Terminal
{
   public static String DEFAULT_KEYBINDINGS_PROPERTIES = "keybindings.properties";

   public static final int DEFAULT_WIDTH = 80;

   public static final int DEFAULT_HEIGHT = 24;

   private Thread shutdownHook;

   private boolean supported;

   private boolean echoEnabled;

   private boolean ansiSupported;

   protected TerminalSupport(final boolean supported)
   {
      this.supported = supported;
   }

   @Override
   public void init() throws Exception
   {
      installShutdownHook(new RestoreHook());
   }

   @Override
   public void restore() throws Exception
   {
      TerminalFactory.resetIf(this);
      removeShutdownHook();
   }

   @Override
   public void reset() throws Exception
   {
      restore();
      init();
   }

   protected void installShutdownHook(final Thread hook)
   {
      assert hook != null;

      if (shutdownHook != null)
      {
         throw new IllegalStateException("Shutdown hook already installed");
      }

      try
      {
         Runtime.getRuntime().addShutdownHook(hook);
         shutdownHook = hook;
      }
      catch (AbstractMethodError e)
      {
         // JDK 1.3+ only method. Bummer.
         org.jboss.forge.shell.console.jline.internal.Log.trace("Failed to register shutdown hook: ", e);
      }
   }

   protected void removeShutdownHook()
   {
      if (shutdownHook != null)
      {
         try
         {
            Runtime.getRuntime().removeShutdownHook(shutdownHook);
         }
         catch (AbstractMethodError e)
         {
            // JDK 1.3+ only method. Bummer.
            org.jboss.forge.shell.console.jline.internal.Log.trace("Failed to remove shutdown hook: ", e);
         }
         catch (IllegalStateException e)
         {
            // The VM is shutting down, not a big deal; ignore
         }
         shutdownHook = null;
      }
   }

   @Override
   public final boolean isSupported()
   {
      return supported;
   }

   @Override
   public synchronized boolean isAnsiSupported()
   {
      return ansiSupported;
   }

   protected synchronized void setAnsiSupported(final boolean supported)
   {
      this.ansiSupported = supported;
      org.jboss.forge.shell.console.jline.internal.Log.debug("Ansi supported: ", supported);
   }

   @Override
   public int getWidth()
   {
      return DEFAULT_WIDTH;
   }

   @Override
   public int getHeight()
   {
      return DEFAULT_HEIGHT;
   }

   @Override
   public synchronized boolean isEchoEnabled()
   {
      return echoEnabled;
   }

   @Override
   public synchronized void setEchoEnabled(final boolean enabled)
   {
      this.echoEnabled = enabled;
      org.jboss.forge.shell.console.jline.internal.Log.debug("Echo enabled: ", enabled);
   }

   @Override
   public int readCharacter(final InputStream in) throws IOException
   {
      return in.read();
   }

   @Override
   public int readVirtualKey(final InputStream in) throws IOException
   {
      return readCharacter(in);
   }

   @Override
   public ResourceBundle getDefaultBindings()
   {
      return ResourceBundle.getBundle("org.jboss.forge.keybindings");
   }

   //
   // RestoreHook
   //

   protected class RestoreHook
            extends Thread
   {
      @Override
      public synchronized void start()
      {
         try
         {
            restore();
         }
         catch (Exception e)
         {
            org.jboss.forge.shell.console.jline.internal.Log.trace("Failed to restore: ", e);
         }
      }
   }
}