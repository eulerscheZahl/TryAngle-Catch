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
      In Wood 1 you can now ATTACK houses
            <!-- END -->
            <!-- BEGIN level3 -->
      In Bronze you can now ADD and REMOVE paths. <br>
      There will be no additional rules in higher leagues.
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
                The map consists of houses, some of which are connected by paths. Houses are defined by x and y
                coordinate.
                Each player starts with <const>3</const> units on one side of the map.
                Then the players try to capture a triangle by owning all three corner houses of it.
                They can use a triangle to spawn a new unit, kill all opponent units on a given house and add or remove a
                path between two houses.

                <br><br>
                <strong>Moving units</strong><br>
                Units can be moved from a house to one of the direct neighbors each turn.

                <br><br>
                <strong>House ownership</strong><br>
                A house is owned, when the player has strictly more units on it than the opponent player or when all
                neighboring houses are owned by that player.

                <br><br>
                <strong>Surrounded units</strong><br>
                When a unit is surrounded by opponent houses, meaning each neighboring house is owned by the opponent, the
                unit is killed.

                <br><br>
                <strong>Triangles</strong><br>
                A triangle is defined by three houses, that are all connected with each other. A triangle may not contain any other houses inside itself.<br>
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
                all units away from each house or after it got captured by the opponent player. <br>
                <!-- BEGIN level2 -->
                <div style="color: #7cc576; background-color: rgba(124, 197, 118,.1); padding: 2px;">
                    <!-- END -->
                    <!-- BEGIN level2 level3 level4 -->
                    Some usages have an additional cost, meaning that the player loses a certain amount of units on each
                    house of the triangle. If the player doesn't have enough units on each house, the triangle can't be
                    used. In order to use a unit, it must have been there for the whole turn and not just moved or
                    spawned there.
                    <!-- END -->
                    <!-- BEGIN level2 -->
                </div>
                <!-- END -->

                <br><br>
                <strong>Spawning a unit</strong><br>
                One way to use a triangle is to spawn a unit. This will place a unit on one house of the triangle. The
                player can choose the corner to spawn on.
                <!-- BEGIN level2 -->
                <span style="color: #7cc576; background-color: rgba(124, 197, 118,.1); padding: 2px;">
                Spawning has no cost.
                </span>
                <!-- END -->
                <!-- BEGIN level3 level4 -->
                Spawning has no cost.
                <!-- END -->
                <!-- BEGIN level2 level3 level4 -->
                <br><br>
                <!-- END -->

                <!-- BEGIN level2 -->
                <div style="color: #7cc576; background-color: rgba(124, 197, 118,.1); padding: 2px;">
                    <!-- END -->
                    <!-- BEGIN level2 level3 level4 -->
                    <strong>Attacking a house</strong><br>
                    A triangle can be used to attack a house, killing all opponent units on it. This action has a cost of
                    <const>1</const>. The attacked house has to be adjacent to at least one house of the used triangle.
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
                    <strong>Adding a path</strong><br>
                    A triangle can be used to create a path between two houses. This can possibly create new triangles
                    as well. This action has a cost of <const>1</const>.
                    The new path must be connected to one house of the used triangle. The path may not cross any existing
                    paths or have a length greater than <const>400</const>.

                    <br><br>
                    <strong>Removing a path</strong><br>
                    A triangle can be used to remove a path between two houses. This can possibly remove triangles as
                    well. This action has a cost of <const>1</const>.
                    The removed path must be connected to one house of the used triangle.
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
                        You collect more points than your opponent
                    </li>
                    <li>
                        You own at least 80% of all triangles
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
                        You collect less points than your opponent
                    </li>
                    <li>
                        You have no triangles and no units left
                    </li>
                    <li>
                        Your opponent owns at least 80% of all triangles
                    </li>
                    <li>
                        You do not respond in time or output an unrecognized command
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
            The source code of the referee can be found on github: <a href="https://github.com/eulerscheZahl/TryAngle-Catch">https://github.com/eulerscheZahl/TryAngle-Catch</a>
            <br>
            <!--There is a debug mode, that you can enable by clicking the gear icon below the player <br>
            <img src="https://cdn-games.codingame.com/community/1500515-1592038359044/2e2e36ce0515e6ef82bbfa5f818c4a359337219f0981bc17b9b159f6114f4f11.png" style="width:100%" />
            -->
            Don't hesitate to change the viewer's options to help debug your code (<img style="opacity:.8;background:#20252a;" height="18" src="https://www.codingame.com/servlet/fileservlet?id=3463235186409" width="18">).
            <br> <br>
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
                <li><span style="color: #7cc576; background-color: rgba(124, 197, 118,.1);">Add paths</span></li>
                <li><span style="color: #7cc576; background-color: rgba(124, 197, 118,.1);">Remove paths</span></li>
                <!-- END -->
                <!-- BEGIN level4 -->
                <li>Add paths</li>
                <li>Remove paths</li>
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
                enough units on each house) and applied if possible.
                Then <const>A1b</const> and <const>A2b</const> will be processed in the same way. It's possible that the first pair of actions renders one of the later ones
                invalid by killing units on a triangle that was supposed to be used later.
                <br><br>
                <!-- END -->
                <!-- BEGIN level2 -->
            </div>
            <!-- END -->
            <!-- BEGIN level3 -->
            <div style="color: #7cc576; background-color: rgba(124, 197, 118,.1); padding: 2px;">
                <!-- END -->
                <!-- BEGIN level3 level4 -->
                For the addition of new paths, the Euclidean distance is used as a tie breaker, giving higher
                priority to shorter
                distances.
                This can make another path addition impossible due to crossing paths. The maps are generated in such a
                way,
                that there are no possible paths with identical distances that can cross each other.
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

                <span class="statement-lineno">First line</span>: <var>houseCount</var>, the number of houses on the board
                <br>
                <span class="statement-lineno">Next <var>houseCount</var> lines</span>: <var>houseId</var> <var>houseX</var>
                <var>houseY</var>, the id and location of each point
            </div>
            <br>
            <br>
            <div class="title">Input for each game turn</div>
            <div class="text">
                <span class="statement-lineno">First line</span>: <var>myScore</var> <var>opponentScore</var>, the score points of you and your opponent respectively
                <br>
                <span class="statement-lineno">Next <var>houseCount</var> lines</span>: <var>houseId</var> <var>myUnits</var> <var>opponentUnits</var>, the house ID and the amount of your own and opponent units on it
                <br>
                <span class="statement-lineno">Next line</span>: <var>linkCount</var>, the number of paths
                <br>
                <span class="statement-lineno">Next <var>linkCount</var> lines</span>: <var>house1</var> <var>house2</var>
                indicating a connection between these two houses
                <br>
                <span class="statement-lineno">Next line</span>: <var>triangleCount</var>, the number of triangles
                <br>
                <span class="statement-lineno">Next <var>triangleCount</var> lines</span>: <var>house1</var>
                <var>house2</var> <var>house3</var> <var>owner</var> <var>meCanCapture</var> <var>opponentCanCapture</var>.
                <br>
                <var>house1</var> <var>house2</var> <var>house3</var> give the corners of the triangle. <br>
                <var>owner</var> indicating the owner of the triangle. It will be <const>-1</const> if it's neutral,
                <const>0</const> if it belongs to you and <const>1</const> if it belongs to your opponent. <br>
                <var>meCanCapture</var> is <const>1</const> if you can currently capture it, <const>0</const> otherwise <br>
                <var>opponentCanCapture</var> is <const>1</const> if your opponent can currently capture it, <const>0</const> otherwise <br>
                <span class="statement-lineno">Next line</span>: <var>linkableCount</var>, the number of new paths that can possibly be created
                <br>
                 <!-- BEGIN level3 -->
                <span style="color: #7cc576; background-color: rgba(124, 197, 118,.1);">From this league you will get possible options for new paths</span> <br>
                <!-- END -->
                 <!-- BEGIN level1 level2 -->
                This value will always be <const>0</const> in lower leagues <br>
                <!-- END -->
                <span class="statement-lineno">Next <var>linkableCount</var> lines</span>: <var>house1</var>
                <var>house2</var> giving the houses that can be linked.
                <br>
                <br>
            </div>

            <div class="title">Output for one game turn</div>
            <div class="text">
                You can print an arbitrary amount of commands per turn, separated by
                <action>;</action>
                <ul style="padding-bottom: 0;">
                    <li>
                        <action>MOVE from to amount</action>
                        move <var>amount</var> units from house <var>from</var> to house <var>to</var>
                    </li>
                    <li>
                        <action>SPAWN house1 house2 house3</action>
                        to spawn a unit on <var>house1</var> using the given triangle. Change the house order to affect
                        the spawning location
                    </li>
                    <!-- BEGIN level2 -->
                    <li><span style="color: #7cc576; background-color: rgba(124, 197, 118,.1);">

                        <action>ATTACK house1 house2 house3 target</action>
                        attack the <var>target</var> house using the given triangle
                    </span></li>
                    <!-- END -->
                    <!-- BEGIN level3 level4 -->
                    <li>
                        <action>ATTACK house1 house2 house3 target</action>
                        attack the <var>target</var> house using the given triangle
                    </li>
                    <!-- END -->

                    <!-- BEGIN level3 -->
                    <li><span style="color: #7cc576; background-color: rgba(124, 197, 118,.1);">
                        <action>ADD_PATH house1 house2 house3 target</action>
                        to connect <var>house1</var> with <var>target</var> using the given triangle.
                    </span></li>
                    <li><span style="color: #7cc576; background-color: rgba(124, 197, 118,.1);">
                          <action>REMOVE_PATH house1 house2 house3 target</action>
                        to disconnect <var>house1</var> from <var>target</var> using the given triangle.
                        </span></li>
                    <!-- END -->
                    <!-- BEGIN level4 -->
                    <li>
                        <action>ADD_PATH house1 house2 house3 target</action>
                        to connect <var>house1</var> with <var>target</var> using the given triangle.
                    </li>
                    <li>
                        <action>REMOVE_PATH house1 house2 house3 target</action>
                        to disconnect <var>house1</var> from <var>target</var> using the given triangle.
                    </li>
                    <li><action>WAIT</action> to do nothing.</li>
                    <!-- END -->
                </ul>

            </div>

            <!-- BEGIN level3 level4 -->
            <br>
            <!-- BEGIN level3 -->
            <div style="color: #7cc576; background-color: rgba(124, 197, 118,.1); padding: 2px;">
            <!-- END -->
            <div class="title">Debug drawing</div>
            <div class="text">
                You can draw additional objects directly into the viewer to help you visualize what your bot is thinking. <br>
                The following commands can be appended to your output commands described above. They have no effect on the gameplay.
                <ul style="padding-bottom: 0;">
                    <li>
                        <action>LINE x1 y1 x2 y2</action>
                        draw a line from (<var>x1</var>, <var>y1</var>) to (<var>x2</var>, <var>y2</var>)
                    </li>
                    <li>
                        <action>RECT x y width height</action>
                        draw a rectangle at (<var>x</var>, <var>y</var>) with a given <var>width</var> and <var>height</var>.
                    </li>
                    <li>
                        <action>CIRCLE x y radius</action>
                        draw a circle at (<var>x</var>, <var>y</var>) with a given <var>radius</var>.
                    </li>
                    <li>
                        <action>TEXT x y message</action>
                        draw a text at (<var>x</var>, <var>y</var>) with a given <var>message</var> of at most <const>20</const>.
                    </li>
                </ul>
                You may create up to <const>50</const> entities for debugging. Entities of the same kind can be reused in later frames.
                By default only your own debug annotations are visible. You can change this in the viewer settings (<img style="opacity:.8;background:#20252a;" height="18" src="https://www.codingame.com/servlet/fileservlet?id=3463235186409" width="18">).
                The color of the debug annotations will match the color of the respective player.
            </div>
            <!-- BEGIN level3 -->
            </div>
            <!-- END -->
            <!-- END -->
        </div>

        <!-- Protocol block -->
        <div class="blk">
            <div class="title">
                Constraints
            </div>
            <div class="text">
                <var>houseCount</var> &le; <const>50</const>
                <br>
                <const>0</const> &le; <var>houseX</var> &lt; <const>1920</const>
                <br>
                <const>0</const> &le; <var>houseY</var> &lt; <const>1080</const>
                <br>
                <br>
                <br> Response time first turn &le; <const>1000</const> ms
                <br> Response time per turn &le; <const>50</const> ms
                <br> The game ends after <const>200</const> turns
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
    <a href="https://craftpix.net/freebies/free-different-sci-fi-item-icons/">https://craftpix.net/freebies/free-different-sci-fi-item-icons</a>
    <br>
</div>
