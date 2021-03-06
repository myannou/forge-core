/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.scaffold;

import java.util.List;

import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.project.Facet;
import org.jboss.forge.resources.Resource;
import org.jboss.forge.shell.plugins.Plugin;

/**
 * Provides an implementation of Scaffolding for various UI code generation operations.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface ScaffoldProvider extends Facet
{
   /**
    * Set up this scaffold provider, installing any necessary {@link Facet} or {@link Plugin} implementations as
    * necessary. Use the given {@link Resource} as a template.
    */
   List<Resource<?>> setup(String targetDir, Resource<?> template, boolean overwrite);

   /**
    * Generate a base set of templates for use in generating resources.
    */
   List<Resource<?>> generateTemplates(String targetDir, boolean overwrite);

   /**
    * Create and configure the index pages for use in this application. Use the given {@link Resource} as a template.
    */
   List<Resource<?>> generateIndex(String targetDir, Resource<?> template, boolean overwrite);

   /**
    * Generate a set of create, read, update, delete pages for a given JPA entity {@link JavaClass}.
    */
   List<Resource<?>> generateFromEntity(String targetDir, Resource<?> template, JavaClass entity, boolean overwrite);

   /**
    * Attempt to locate all {@link Resource}s generated by this {@link ScaffoldProvider}.
    */
   List<Resource<?>> getGeneratedResources(String targetDir);

   /**
    * Get the {@link AccessStrategy} for this {@link ScaffoldProvider}.
    */
   AccessStrategy getAccessStrategy();

   /**
    * Get the {@link TemplateStrategy} for this {@link ScaffoldProvider}.
    */
   TemplateStrategy getTemplateStrategy();

}
