/*
* JBoss, Home of Professional Open Source
* Copyright 2006, JBoss Inc., and individual contributors as indicated
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/
package org.jboss.deployers.plugins.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jboss.deployers.spi.Ordered;

/**
 * Simple transition ordering using transitive closure.
 * 
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 * @param <T> exact domino type
 */
public class DominoOrdering<T extends Domino>
{
   private static final Ordered.OrderedComparator ORDERED_COMPARATOR = new Ordered.OrderedComparator();

   private String message;

   private List<T> dominoes;
   private int size;
   private int[][] connections;

   public DominoOrdering(String message)
   {
      this.message = message;
   }

   @SuppressWarnings("unchecked")
   protected void init(List<T> dominoes)
   {
      this.dominoes = dominoes;
      this.size = dominoes.size();
      this.connections = new int[size][size];

      for (int i = 0; i < size - 1; i++)
      {
         for (int j = i + 1; j < size; j++)
         {
            Domino one = dominoes.get(i);
            Domino two = dominoes.get(j);
            Dots oneHead = one.getHead();
            Dots oneTail = one.getTail();
            Dots twoHead = two.getHead();
            Dots twoTail = two.getTail();
            boolean fstXsnd = oneTail.match(twoHead);
            boolean sndXfst = twoTail.match(oneHead);
            int relation = 0;
            if (fstXsnd && sndXfst)
            {
               // pass-through deployers
               if (one.getHead().match(twoHead) && oneTail.match(twoTail))
                  relation = ORDERED_COMPARATOR.compare(one, two);
               else
                  // short circut cycle - throw exception immediately
                  throwCycleException(i);
            }
            else
               relation = fstXsnd ? -1 : (sndXfst ? 1 : 0);
            if (relation == 0)
               relation = one.getRelativeOrder() - two.getRelativeOrder();
            connections[i][j] = relation;
            connections[j][i] = -connections[i][j];
         }
      }

   }

   public List<T> orderDominoes(List<T> dominoes)
   {
      init(dominoes);
      fillTransitions();
      List<Integer> indexes = new ArrayList<Integer>();
      for (int i = 0; i < size; i++)
         indexes.add(i);
      Collections.sort(indexes, new IndexComparator());
      List<T> list = new ArrayList<T>(size);
      for(Integer index : indexes)
         list.add(dominoes.get(index));
      return list;
   }

   protected void fillTransitions()
   {
      boolean changed = true;
      while(changed)
      {
         changed = false;
         for (int i = 0; i < size; i++)
         {
            for (int j = 0; j < size; j++)
            {
               if (j == i || connections[i][j] == 0)
                  continue;
               for (int k = 0; k < size; k++)
               {
                  if (k == i || k == j)
                     continue;
                  if (connections[i][j] > 0)
                  {
                     if (connections[j][k] > 0)
                     {
                        // already set
                        if (connections[i][k] > 0)
                           continue;
                        // cycle
                        else if (connections[i][k] < 0)
                           throwCycleException(i);
                        connections[i][k] = 1;
                        changed = true;
                     }
                  }
                  else if (connections[i][j] < 0)
                  {
                     if (connections[j][k] < 0)
                     {
                        // already set
                        if (connections[i][k] < 0)
                           continue;
                        // cycle
                        else if (connections[i][k] > 0)
                           throwCycleException(i);
                        connections[i][k] = -1;
                        changed = true;
                     }
                  }
               }
            }
         }
      }
   }

   protected void throwCycleException(int index)
   {
      StringBuilder builder = new StringBuilder();
      builder.append(String.format(message, dominoes.get(index)));
      for (T d : dominoes)
         builder.append(d.getInfo());
      throw new IllegalStateException(builder.toString());
   }

   private class IndexComparator implements Comparator<Integer>
   {
      public int compare(Integer i1, Integer i2)
      {
         return connections[i1][i2];
      }
   }

   protected void print()
   {
      for(int i = 0; i < size; i++)
      {
         for(int j = 0; j < size; j++)
         {
            System.out.print(String.format("%2d ", connections[i][j]));
         }
         System.out.println();
      }
      System.out.println();
   }

}
