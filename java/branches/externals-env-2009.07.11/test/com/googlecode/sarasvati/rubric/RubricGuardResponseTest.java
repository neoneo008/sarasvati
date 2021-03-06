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

    Copyright 2008-2009 Paul Lorenz
*/

package com.googlecode.sarasvati.rubric;

import junit.framework.Assert;

import org.junit.Test;

import com.googlecode.sarasvati.GuardResponse;
import com.googlecode.sarasvati.impl.SkipNodeGuardResponse;

public class RubricGuardResponseTest
{
  @Test public void testAccept ()
  {
    GuardResponse expected = GuardResponse.ACCEPT_TOKEN_RESPONSE;
    String script="Accept";
    System.out.println( "SCRIPT: " + script );

    Object result = RubricInterpreter.compile( script ).eval( TestRubricEnv.INSTANCE );

    Assert.assertEquals( expected, result );
  }

  @Test public void testDiscard ()
  {
    GuardResponse expected = GuardResponse.DISCARD_TOKEN_RESPONSE;
    String script="Discard";
    System.out.println( "SCRIPT: " + script );

    Object result = RubricInterpreter.compile( script ).eval( TestRubricEnv.INSTANCE );

    Assert.assertEquals( expected, result );
  }

  @Test public void testSkip ()
  {
    GuardResponse expected = SkipNodeGuardResponse.DEFAULT_ARC_SKIP_NODE_RESPONSE;
    String script="Skip";
    System.out.println( "SCRIPT: " + script );

    Object result = RubricInterpreter.compile( script ).eval( TestRubricEnv.INSTANCE );

    Assert.assertEquals( expected, result );
  }

  @Test public void testSkipWithArcName ()
  {
    GuardResponse expected = new SkipNodeGuardResponse( "testArc" );
    String script="Skip testArc";
    System.out.println( "SCRIPT: " + script );

    Object result = RubricInterpreter.compile( script ).eval( TestRubricEnv.INSTANCE );

    Assert.assertEquals( expected, result );
  }
}