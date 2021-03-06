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

    Copyright 2008 Paul Lorenz
*/
package com.googlecode.sarasvati;

import org.junit.Test;

public class BacktrackSplitTest extends ExecutionTest
{
  @Test public void testSplit() throws Exception
  {
    Graph g = ensureLoaded( "backtrack-split" );
    GraphProcess p = engine.startProcess( g );

    NodeToken tokenA = getActiveToken( p, "nodeA" );

    String state = "[1 nodeA I F]";
    TestProcess.validate( p, state );

    engine.complete( tokenA, Arc.DEFAULT_ARC );

    state =
      "[1 nodeA C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "  (C F 4)" +
      "[2 nodeB I F]" +
      "[3 nodeC I F]" +
      "[4 nodeD I F]"
      ;
    TestProcess.validate( p, state );

    engine.backtrack( tokenA );

    state =
      "[1 nodeA C FB]" +
      "  (C FB 2)" +
      "  (C FB 3)" +
      "  (C FB 4)" +
      "[2 nodeB C FB]" +
      "  (C B 5)" +
      "[3 nodeC C FB]" +
      "  (C B 5)" +
      "[4 nodeD C FB]" +
      "  (C B 5)" +
      "[5 nodeA I F]"
      ;

    TestProcess.validate( p, state );

    tokenA = p.getActiveNodeTokens().iterator().next();
    engine.complete( tokenA, Arc.DEFAULT_ARC );
    state =
      "[1 nodeA C FB]" +
      "  (C FB 2)" +
      "  (C FB 3)" +
      "  (C FB 4)" +
      "[2 nodeB C FB]" +
      "  (C B 5)" +
      "[3 nodeC C FB]" +
      "  (C B 5)" +
      "[4 nodeD C FB]" +
      "  (C B 5)" +
      "[5 nodeA C F]" +
      "  (C F 6)" +
      "  (C F 7)" +
      "  (C F 8)" +
      "[6 nodeB I F]" +
      "[7 nodeC I F]" +
      "[8 nodeD I F]"
      ;

    TestProcess.validate( p, state );

    engine.backtrack( tokenA );

    state =
      "[1 nodeA C FB]" +
      "  (C FB 2)" +
      "  (C FB 3)" +
      "  (C FB 4)" +
      "[2 nodeB C FB]" +
      "  (C B 5)" +
      "[3 nodeC C FB]" +
      "  (C B 5)" +
      "[4 nodeD C FB]" +
      "  (C B 5)" +
      "[5 nodeA C FB]" +
      "  (C FB 6)" +
      "  (C FB 7)" +
      "  (C FB 8)" +
      "[6 nodeB C FB]" +
      "  (C B 9)" +
      "[7 nodeC C FB]" +
      "  (C B 9)" +
      "[8 nodeD C FB]" +
      "  (C B 9)" +
      "[9 nodeA I F]"
      ;

    TestProcess.validate( p, state );
  }

  @Test public void testSplitDeadEnd() throws Exception
  {
    Graph g = ensureLoaded( "backtrack-split" );
    GraphProcess p = engine.startProcess( g );

    NodeToken tokenA = getActiveToken( p, "nodeA" );

    String state = "[1 nodeA I F]";
    TestProcess.validate( p, state );

    engine.complete( tokenA, Arc.DEFAULT_ARC );

    state =
      "[1 nodeA C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "  (C F 4)" +
      "[2 nodeB I F]" +
      "[3 nodeC I F]" +
      "[4 nodeD I F]";
    TestProcess.validate( p, state );

    NodeToken tokenB = getActiveToken( p,"nodeB" );
    engine.complete( tokenB, Arc.DEFAULT_ARC );

    state =
      "[1 nodeA C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "  (C F 4)" +
      "[2 nodeB C F]" +
      "[3 nodeC I F]" +
      "[4 nodeD I F]";
    TestProcess.validate( p, state );

    NodeToken tokenC = getActiveToken( p,"nodeC" );
    engine.complete( tokenC, Arc.DEFAULT_ARC );

    state =
      "[1 nodeA C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "  (C F 4)" +
      "[2 nodeB C F]" +
      "[3 nodeC C F]" +
      "[4 nodeD I F]";
    TestProcess.validate( p, state );

    engine.backtrack( tokenB );

    state =
      "[1 nodeA C F]" +
      "  (C FB 2)" +
      "  (C F 3)" +
      "  (C F 4)" +
      "[2 nodeB C FB]" +
      "  (C U 5)" +
      "[3 nodeC C F]" +
      "[4 nodeD I F]" +
      "[5 nodeB I F]";

    TestProcess.validate( p, state );

    tokenB = getActiveToken( p,"nodeB" );
    engine.complete( tokenB, Arc.DEFAULT_ARC );

    state =
      "[1 nodeA C F]" +
      "  (C FB 2)" +
      "  (C F 3)" +
      "  (C F 4)" +
      "[2 nodeB C FB]" +
      "  (C U 5)" +
      "[3 nodeC C F]" +
      "[4 nodeD I F]" +
      "[5 nodeB C F]";

    TestProcess.validate( p, state );

    engine.backtrack( tokenB );

    state =
      "[1 nodeA C F]" +
      "  (C FB 2)" +
      "  (C F 3)" +
      "  (C F 4)" +
      "[2 nodeB C FB]" +
      "  (C UB 5)" +
      "[3 nodeC C F]" +
      "[4 nodeD I F]" +
      "[5 nodeB C FB]" +
      "  (C U 6)" +
      "[6 nodeB I F]";

    TestProcess.validate( p, state );

    engine.backtrack( tokenA );

    state =
      "[1 nodeA C FB]" +
      "  (C FB 2)" +
      "  (C FB 3)" +
      "  (C FB 4)" +
      "[2 nodeB C FB]" +
      "  (C UB 5)" +
      "[3 nodeC C FB]" +
      "  (C B 7)" +
      "[4 nodeD C FB]" +
      "  (C B 7)" +
      "[5 nodeB C FB]" +
      "  (C UB 6)" +
      "[6 nodeB C FB]" +
      "  (C B 7)" +
      "[7 nodeA I F]";

    TestProcess.validate( p, state );
  }
}