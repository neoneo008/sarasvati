/*
    This file is part of Sarasvati.

    Sarasvati is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    Sarasvati is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with Sarasvati.  If not, see <http://www.gnu.org/licenses/>.

    Copyright 2009 Paul Lorenz
*/

package com.googlecode.sarasvati.load.properties;

import java.beans.PropertyDescriptor;

import com.googlecode.sarasvati.load.LoadException;

public class EditorLoadPropertyMutator implements PropertyMutator
{
  public static final EditorLoadPropertyMutator INSTANCE = new EditorLoadPropertyMutator();

  @Override
  public Object getCurrentValue () throws LoadException
  {
    return this;
  }

  @Override
  public void setFromText (String text) throws LoadException
  {
    // does nothing
  }

  @Override
  public void setPropertyDescriptor (PropertyDescriptor pd)
  {
    // does nothing
  }

  @Override
  public void setTarget (Object target)
  {
    // does nothing
  }

  @Override
  public void setValue (Object value) throws LoadException
  {
    // does nothing
  }
}
