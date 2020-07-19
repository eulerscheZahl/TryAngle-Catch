<!-- LEAGUES level1 level2 level3 level4 -->
<div id="statement_back" class="statement_back" style="display:none" xmlns="http://www.w3.org/1999/html"></div>
<div class="statement-body">
    <!-- BEGIN level1 level2 level3 -->
    <div style="color: #7cc576;
        background-color: rgba(124, 197, 118,.1);
        padding: 20px;
        margin-right: 15px;
        margin-left: 15px;
        margin-bottom: 10px;
        text-align: left;">
        <div style="text-align: center; margin-bottom: 6px">
            <img src="//cdn.codingame.com/smash-the-code/statement/league_wood_04.png" alt="Wood League"/>
        </div>
        <p style="text-align: center; font-weight: 700; margin-bottom: 6px;">
            <!-- BEGIN level1 -->
            This is a <b>league-based</b> challenge.
            <!-- END -->
            <!-- BEGIN level2 -->
            Welcome to the Wood1 league!
            <!-- END -->
            <!-- BEGIN level3 -->
            Welcome to the Bronze league!
            <!-- END -->
        </p>
        <span class="statement-league-alert-content">
      <!-- BEGIN level1 -->
      Wood leagues should be considered as a tutorial which lets players discover the different rules of the game. <br>
      In Bronze league all rules will be unlocked and the real challenge will begin.
            <!-- END -->
            <!-- BEGIN level2 -->
      In Wood 1 you can now ATTACK nodes
            <!-- END -->
            <!-- BEGIN level3 -->
      In Bronze you can now ADD and REMOVE edges.
            <!-- END -->
    </span>
<!-- BEGIN level1 level2 -->
        <br>To have a look at the final rules, go <a href="https://www.codingame.com/ide/demo/843364c35de5a48f4e0de3a6e0720b74d11b1a">here</a> and switch leagues at the top right corner.
        <!-- END -->
    </div>
    <!-- END -->

    <!-- GOAL -->
    <div class="statement-section statement-goal">
        <h1>
            <span class="icon icon-goal">&nbsp;</span>
            <span>The Goal</span>
        </h1>
        <div class="statement-goal-content">
            Collect more points than your opponent by capturing and defending triangles.
        </div>
    </div>
    <!-- RULES -->
    <div class="statement-section statement-rules">
        <h1>
            <span class="icon icon-rules">&nbsp;</span>
            <span>Rules</span>
        </h1>
        <div>
            <div class="statement-rules-content">
                The map consists of nodes, some of which are connected by edges. Nodes are defined by x and y
                coordinate.
                Each player starts with <const>3</const> units on one side of the map.
                You then try to capture a triangle by owning all tree corner nodes of it.
                You can use a triangle to spawn a new unit, kill all opponent units on a given node, add or remove a
                connection between two nodes.

                <br><br>
                <strong>Moving units</strong><br>
                Units can be moved from a node to one of the direct neighbors each turn.

                <br><br>
                <strong>Node ownership</strong><br>
                A node is owned, when the player has strictly more units on it than the opponent player or when all
                neighboring nodes are owned by that player.

                <br><br>
                <strong>Surrounded units</strong><br>
                When a unit is surrounded by opponent nodes, meaning each neighboring node is owned by the opponent, the
                unit is killed.

                <br><br>
                <strong>Triangles</strong><br>
                A triangle is defined by three nodes, that are all connected with each other.<br>
                After each turn the players get <const>1</const> point for each triangle they currently own.

                <br><br>
                <strong>Capturing a triangle</strong><br>
                To capture a triangle, a player has to own all three corners of it and be eligible to claim the
                triangle.
                Initially the players can capture all triangles. This will only change after using them.
                A captured triangle will remain in the ownership of the player until it's used or captured by the
                opponent.

                <br><br>
                <strong>Using a triangle</strong><br>
                When owning a triangle, a player can use it to perform one of the actions described below.
                This will make the triangle neutral again. The player using it can only capture it again after moving
                away all units from each node or after it got captured by the opponent player. <br>
                <!-- BEGIN level2 -->
                <div style="color: #7cc576; background-color: rgba(124, 197, 118,.1); padding: 2px;">
                    <!-- END -->
                    <!-- BEGIN level2 level3 level4 -->
                    Some usages have an additional cost, meaning that the player loses a certain amount of units on each
                    node of the triangle. If the player doesn't have enough units on each node, the triangle can't be
                    used. In order to use a unit, it must have been there for the whole turn and not just moved or
                    spawned there.
                    <!-- END -->
                    <!-- BEGIN level2 -->
                </div>
                <!-- END -->

                <br><br>
                <strong>Spawning a unit</strong><br>
                One way to use a triangle is to spawn a unit. This will place a unit on one node of the triangle. The
                player can decide the corner to spawn on. Spawning has no cost.

                <!-- BEGIN level2 level3 level4 -->
                <br><br>
                <!-- END -->
                <!-- BEGIN level2 -->
                <div style="color: #7cc576; background-color: rgba(124, 197, 118,.1); padding: 2px;">
                    <!-- END -->
                    <!-- BEGIN level2 level3 level4 -->
                    <strong>Attacking a node</strong><br>
                    A triangle can be used to attack a node, killing all opponent units on it. This action has a cost of
                    <const>1</const>. The attacked node has to be adjacent to at least one node of the used triangle.
                    <!-- END -->
                    <!-- BEGIN level2 -->
                </div>
                <!-- END -->

                <!-- BEGIN level3 level4 -->
                <br><br>
                <!-- END -->
                <!-- BEGIN level3 -->
                <div style="color: #7cc576; background-color: rgba(124, 197, 118,.1); padding: 2px;">
                    <!-- END -->
                    <!-- BEGIN level3 level4 -->
                    <strong>Adding an edge</strong><br>
                    A triangle can be used to create an edge between two nodes. This can possibly create new triangles
                    as well. This action has a cost of <const>1</const>.
                    The new edge must be connected to one node of the used triangle. The edge may not cross any existing
                    edges.

                    <br><br>
                    <strong>Removing an edge</strong><br>
                    A triangle can be used to remove an edge between two nodes. This can possibly remove triangles as
                    well. This action has a cost of <const>1</const>.
                    The removed edge must be connected to one node of the used triangle.
                    <!-- END -->
                    <!-- BEGIN level3 -->
                </div>
                <!-- END -->
            </div>
        </div>
    </div>

    <!-- Victory conditions -->
    <div class="statement-victory-conditions">
        <div class="icon victory"></div>
        <div class="blk">
            <div class="title">Victory Conditions</div>
            <div class="text">
                <ul style="padding-bottom: 0;">
                    <li>
                        Collect more points than your opponent
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <!-- Lose conditions -->
    <div class="statement-lose-conditions">
        <div class="icon lose"></div>
        <div class="blk">
            <div class="title">Loss Conditions</div>
            <div class="text">
                <ul style="padding-bottom: 0;">
                    <li>
                        Collect less points than your opponent
                    </li>
                    <li>
                        You do not respond in time or output an unrecognized command.
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <br>

    <!-- EXPERT RULES -->
    <div class="statement-section statement-expertrules">
        <h1>
            <span class="icon icon-expertrules">&nbsp;</span>
            <span>Expert Rules</span>
        </h1>
        <div class="statement-expert-rules-content">
            The game turn works as follows:
            <ol>
                <!-- BEGIN level2 -->
                <li><span style="color: #7cc576; background-color: rgba(124, 197, 118,.1);">
                Apply attack actions</span></li>
                <!-- END -->
                <!-- BEGIN level3 level4 -->
                <li>Apply attack actions</li>
                <!-- END -->
                <li>Move units</li>
                <li>Spawn units</li>
                <!-- BEGIN level3 -->
                <li><span style="color: #7cc576; background-color: rgba(124, 197, 118,.1);">Add edges</span></li>
                <li><span style="color: #7cc576; background-color: rgba(124, 197, 118,.1);">Remove edges</span></li>
                <!-- END -->
                <!-- BEGIN level4 -->
                <li>Add edges</li>
                <li>Remove edges</li>
                <!-- END -->
                <li>Kill surrounded units</li>
                <li>Change ownership of triangles</li>
            </ol>
            <!-- BEGIN level2 -->
            <div style="color: #7cc576; background-color: rgba(124, 197, 118,.1); padding: 2px;">
                <!-- END -->
                <!-- BEGIN level2 level3 level4 -->
                Within the same type of action, actions are applied for both players simultaneously, but sequentially
                for the
                same player. <br>
                Example: <br>
                Player 1 wants to perform two attack actions <const>A1a</const> and <const>A1b</const>.
                Player 2 wants to perform two attack actions <const>A2a</const> and <const>A2b</const>.
                First <const>A1a</const> and <const>A2a</const> will be checked for being valid (the triangles in use are owned by the respective players and they have
                enough units on each node) and applied if possible.
                Then <const>A1b</const> and <const>A2b</const> will be processed in the same way. It's possible that the first group of actions renders on of them
                invalid by killing units on a triangle that was supposed to be used later.
                <br><br> <!-- END -->
                <!-- BEGIN level2 -->
            </div>
            <!-- END -->
            <!-- BEGIN level3 -->
            <div style="color: #7cc576; background-color: rgba(124, 197, 118,.1); padding: 2px;">
                <!-- END -->
                <!-- BEGIN level3 level4 -->
                For the addition of new edges, the pythagorean distance is used as a tie breaker, giving higher
                priority to shorter
                distances.
                This can make another edge addition impossible due to crossing edges. The maps are generated in such a
                way,
                that there are no possible edges with identical distances that can cross each other. <!-- END -->
                <!-- END -->
                <!-- BEGIN level3 -->
            </div>
            <!-- END -->
        </div>
    </div>

    <!-- PROTOCOL -->
    <div class="statement-section statement-protocol">
        <h2>
            <span class="icon icon-protocol">&nbsp;</span>
            <span>Game Input</span>
        </h2>
        <!-- Protocol block -->
        <div class="blk">
            <div class="title">Initial input</div>
            <div class="text">

                <span class="statement-lineno">First line</span>: <var>nodeCount</var>, the number of nodes on the board
                <br>
                <span class="statement-lineno">Next <var>nodeCount</var> lines</span>: <var>nodeId</var> <var>x</var>
                <var>y</var>, the id and location of each point
            </div>
            <br>
            <br>
            <div class="title">Input for one game turn</div>
            <div class="text">
                <span class="statement-lineno">First line</span>: <var>nodeCount</var> space separated numbers giving
                <var>myUnits</var> for each node
                <br>
                <span class="statement-lineno">Second line</span>: <var>nodeCount</var> space separated numbers giving
                <var>opponentUnits</var> for each node
                <br>
                <span class="statement-lineno">Third line</span>: <var>linkCount</var>, the number of edges
                <br>
                <span class="statement-lineno">Next <var>linkCount</var> lines</span>: <var>node1</var> <var>node2</var>
                indicating a connection between these two nodes
                <br>
                <span class="statement-lineno">Next line</span>: <var>triangleCount</var>, the number of triangles
                <br>
                <span class="statement-lineno">Next <var>triangleCount</var> lines</span>: <var>node1</var>
                <var>node2</var> <var>node3</var> <var>owner</var> <var>meCanCapture</var> <var>opponentCanCapture</var>.
                <br>
                <var>node1</var> <var>node2</var> <var>node3</var> give the corners of the triangle. <br>
                <var>owner</var> indicating the owner of the triangle. It will be <const>-1</const> if it's neutral,
                <const>0</const> if it belongs to you and <const>1</const> if it belongs to your opponent. <br>
                <var>meCanCapture</var> is <const>1</const> if you can currently capture it, <const>0</const> otherwise <br>
                <var>opponentCanCapture</var> is <const>1</const> if your opponent can currently capture it, <const>0</const> otherwise <br>
                <br>
            </div>

            <div class="title">Output for one game turn</div>
            <div class="text">
                You can print an arbitrary amount of commands per turn, separated by
                <action>;</action>
                <ul style="padding-bottom: 0;">
                    <li>
                        <action>MOVE from to amount</action>
                        move <var>amount</var> units from node <var>from</var> to node <var>to</var>
                    </li>
                    <li>
                        <action>SPAWN node1 node2 node3</action>
                        to spawn a unit on <var>node1</var> using the given triangle. Change the node order to affect
                        the spawning location
                    </li>
                    <!-- BEGIN level2 -->
                    <li><span style="color: #7cc576; background-color: rgba(124, 197, 118,.1);">

                        <action>ATTACK node1 node2 node3 target</action>
                        attack the <var>target</var> node using the given triangle
                    </span></li>
                    <!-- END -->
                    <!-- BEGIN level3 level4 -->
                    <li>
                        <action>ATTACK node1 node2 node3 target</action>
                        attack the <var>target</var> node using the given triangle
                    </li>
                    <!-- END -->

                    <!-- BEGIN level3 -->
                    <li><span style="color: #7cc576; background-color: rgba(124, 197, 118,.1);">
                        <action>ADD_EDGE node1 node2 node3 target</action>
                        to connect <var>node1</var> with <var>target</var> using the given triangle.
                    </span></li>
                    <li><span style="color: #7cc576; background-color: rgba(124, 197, 118,.1);">
                          <action>REMOVE_EDGE node1 node2 node3 target</action>
                        to disconnect <var>node1</var> from <var>target</var> using the given triangle.
                        </span></li>
                    <!-- END -->
                    <!-- BEGIN level4 -->
                    <li>
                        <action>ADD_EDGE node1 node2 node3 target</action>
                        to connect <var>node1</var> with <var>target</var> using the given triangle.
                    </li>
                    <li>
                        <action>REMOVE_EDGE node1 node2 node3 target</action>
                        to disconnect <var>node1</var> from <var>target</var> using the given triangle.
                    </li>
                    <!-- END -->
                </ul>

            </div>
        </div>

        <!-- Protocol block -->
        <div class="blk">
            <div class="title">
                Constraints
            </div>
            <div class="text">
                <var>nodeCount</var> &le; <const>50</const>
                <br>
                <const>0</const> &le; <var>nodeX</var> &lt; <const>1920</const>
                <br>
                <const>0</const> &le; <var>nodeY</var> &lt; <const>1080</const>
                <br>
                <br>
                <br> Response time first turn &le; <const>1000</const> ms
                <br> Response time per turn &le; <const>50</const> ms
            </div>
        </div>
    </div>

    Sprites: <br>
    <a href="https://craftpix.net/freebies/free-tropical-medieval-city-2d-tileset/">https://craftpix.net/freebies/free-tropical-medieval-city-2d-tileset/</a>
    <br>
    <a href="https://craftpix.net/freebies/2d-fantasy-knight-free-sprite-sheets/">https://craftpix.net/freebies/2d-fantasy-knight-free-sprite-sheets/</a>
    <br>
    <a href="https://craftpix.net/freebies/free-zombie-tds-tilesets-buildings-and-furniture/">https://craftpix.net/freebies/free-zombie-tds-tilesets-buildings-and-furniture/</a>
    <br>
</div>