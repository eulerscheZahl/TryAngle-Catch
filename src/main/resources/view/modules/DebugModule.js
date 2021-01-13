import { api as entityModule } from '../entity-module/GraphicEntityModule.js'
import { EntityFactory } from '../entity-module/EntityFactory.js'
import { TinyToggleModule } from './TinyToggleModule.js'
import { graphicsHelper } from './TaskModule.js'

export class DebugModule {

    constructor(assets) {
        this.cache = []
        this.turnCache = []
        DebugModule.refreshContent = () => {}
    }

    static refreshContent() {}

    static get name() {
        return 'debug'
    }

    updateScene(previousData, currentData, progress) {}

    getEntity(type, playerIndex) {
        var toggleShow = [playerIndex + 3, 5]
        if (this.isMe[playerIndex]) toggleShow.push(1)
        else toggleShow.push(2)

        for (var i = 0; i < this.cache.length; i++) {
            if (this.cache[i].type == type && this.cache[i].player == playerIndex) {
                var result = this.cache[i];
                this.cache.splice(i, 1);
                this.turnCache.push(result);
                return result.entity;
            }
        }

        var result = EntityFactory.create(type);
        result.id = ++graphicsHelper.runtimeId;
        entityModule.entities.set(result.id, result);
        this.turnCache.push({ type: type, entity: result, player: playerIndex });
        TinyToggleModule.instance.registerToggle(result, "draw", toggleShow)
        return result;
    }

    handleFrameData(frameInfo, data) {
        if (!data) return;

        var playerIndex = 0;
        data.split(';').forEach(d => {
            if (d == '') playerIndex = 1;
            else {
                var p = d.substr(1).split(' ')
                var entity = this.getEntity(d[0], playerIndex);
                var params = { x: +p[0], y: +p[1] }
                if (d[0] == 'L') {
                    params = { ...graphicsHelper.defaults.line, ...params, x2: +p[2], y2: +p[3], t: 0,
                        lineColor: graphicsHelper.playerColors[playerIndex], lineWidth: 3, zIndex: 10 }
                }
                if (d[0] == 'R') {
                    params = { ...graphicsHelper.defaults.rect, ...params, width: +p[2], height: +p[3], t: 0,
                        lineColor: graphicsHelper.playerColors[playerIndex], lineWidth: 3, zIndex: 10, fillAlpha: 0 }
                }
                if (d[0] == 'C') {
                    params = { ...graphicsHelper.defaults.circle, ...params, radius: +p[2], t: 0,
                        lineColor: graphicsHelper.playerColors[playerIndex], lineWidth: 3, zIndex: 10, fillAlpha: 0 }
                }
                if (d[0] == 'T') {
                    p.splice(0, 2)
                    params = { ...graphicsHelper.defaults.text, ...params, text: p.join(" "), t: 0, fontSize: 25,
                        fillColor: graphicsHelper.playerColors[playerIndex], lineWidth: 3, zIndex: 10 }
                }
                entity.addState(0, { values: params, curve: {}  }, frameInfo.number, frameInfo)
            }
        });

        for (var i = 0; i < this.cache.length; i++) {
            var entity = this.cache[i].entity;
            entity.addState(0, {
                values: entity.defaultState,
                curve: {}
            }, frameInfo.number, frameInfo)
        }
        this.cache.push(...this.turnCache)
        this.turnCache = []
        return {}
    }

    handleGlobalData(players, globalData) {
        this.isMe = [players[0].isMe, players[1].isMe];
    }

    reinitScene(container, canvasData) {}
}