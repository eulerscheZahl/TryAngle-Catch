import { api as entityModule } from '../entity-module/GraphicEntityModule.js'
import { EntityFactory } from '../entity-module/EntityFactory.js'
import { TinyToggleModule } from './TinyToggleModule.js'
import { NodeModule } from './NodeModule.js'

export const graphicsHelper = {
    runtimeId: 1000000, // don't interfere with counter from SDK, as this would update the wrong entities from Java code
    playerColors: [0xff4040, 0x4040ff],
    defaults: {
        sprite: { alpha: 1, anchorX: 0, anchorY: 0, baseHeight: null, baseWidth: null, blendMode: 0, curve: {}, image: null, mask: -1, rotation: 0, scaleMode: "LINEAR", scaleX: 1, scaleY: 1, skewX: 0, skewY: 0, t: 0, tint: 0xFFFFFF, visible: true, x: 0, y: 0, zIndex: 0, },
        circle: { alpha: 1, blendMode: 0, curve: {}, fillAlpha: 1, fillColor: 0xFFFFFF, lineAlpha: 1, lineColor: 0, lineWidth: 1, mask: -1, radius: 25, rotation: 0, scaleX: 1, scaleY: 1, skewX: 0, skewY: 0, t: 0, visible: true, x: 0, y: 0, zIndex: 0, },
        group: { alpha: 1, children: [], mask: -1, rotation: 0, scaleX: 1, scaleY: 1, skewX: 0, skewY: 0, visible: true, x: 0, y: 0, zIndex: 0, },
        text: { alpha: 1, anchorX: 0, anchorY: 0, blendMode: 0, fillColor: 0, fontFamily: "Lato", fontSize: 26, fontWeight: "normal", mask: -1, maxWidth: 0, rotation: 0, scaleX: 1, scaleY: 1, skewX: 0, skewY: 0, strokeColor: 0, strokeThickness: 0, text: "", textAlign: "left", tint: 16777215, visible: true, x: 0, y: 0, zIndex: 0, },
        line: { alpha: 1, blendMode: 0, curve: {}, fillAlpha: 1, fillColor: 0xFFFFFF, lineAlpha: 1, lineColor: 0, lineWidth: 1, mask: -1, rotation: 0, scaleX: 1, scaleY: 1, skewX: 0, skewY: 0, t: 0, visible: true, x: 0, y: 0, zIndex: 0, },
        rect: { alpha: 1, blendMode: 0, curve: {}, fillAlpha: 1, fillColor: 0xFFFFFF, lineAlpha: 1, lineColor: 0, lineWidth: 1, mask: -1, width: 25, height: 25, rotation: 0, scaleX: 1, scaleY: 1, skewX: 0, skewY: 0, t: 0, visible: true, x: 0, y: 0, zIndex: 0, },
    }
}

export class TaskModule {

    constructor(assets) {
        this.knightCache = []
        this.knightTurnCache = []
        this.spriteCache = []
        this.spriteTurnCache = []

        TaskModule.refreshContent = () => {}
    }

    static refreshContent() {}

    static get name() {
        return 'tasks'
    }

    updateScene(previousData, currentData, progress) {}

    getOffsetNode(node, player) {
        const offsetX = -40 + 80 * player
        const offsetY = -40
        return {
            x: node.x + offsetX,
            y: node.y + offsetY
        }
    }

    generateMoveAnimation(frameInfo, move) {
        const from = this.getOffsetNode(move.from, move.player)
        const to = this.getOffsetNode(move.to, move.player)
        this.performMove(frameInfo, move.player, from, to, move.amount)
    }

    generateSpawnAnimation(frameInfo, spawn) {
        const to = this.getOffsetNode(spawn.to, spawn.player)
        this.performMove(frameInfo, spawn.player, spawn.from, to, 1)
    }

    generateTaskAnimation(frameInfo, task) {
        this.performMove(frameInfo, task.player, this.getOffsetNode(task.node1, task.player), task.triangle, 1)
        this.performMove(frameInfo, task.player, this.getOffsetNode(task.node2, task.player), task.triangle, 1)
        this.performMove(frameInfo, task.player, this.getOffsetNode(task.node3, task.player), task.triangle, 1)


        if (task.type === "A" && task.amount > 0) {
            const to = this.getOffsetNode(task.target, 1 - task.player)
            this.performMove(frameInfo, (1 - task.player), to, to, task.amount, {}, { alpha: 0 })
            this.animateAttack(frameInfo, task.player, task.triangle, to)
        }
    }

    animateAttack(frameInfo, player, from, to) {
        if (this.spriteCache.length > 0) {
            var sprite = this.spriteCache.pop()
        } else {
            var sprite = EntityFactory.create("S")
            sprite.id = ++graphicsHelper.runtimeId
            entityModule.entities.set(sprite.id, sprite)
            TinyToggleModule.instance.registerToggle(sprite, "d", false)
        }

        var spriteParams = { ...graphicsHelper.defaults.sprite, x: from.x, y: from.y, zIndex: 9, image: "x" + "rb" [player] + ".png", t: 0.5, scaleX: 0.1, scaleY: 0.1, anchorX: 0.5, anchorY: 0.5 }
        sprite.addState(0.5, { values: spriteParams, curve: {} }, frameInfo.number, frameInfo)
        var spriteMoveParams = { ...spriteParams, x: to.x, y: to.y, t: 1, rotation: 180, visible: false }
        sprite.addState(1, { values: spriteMoveParams, curve: {} }, frameInfo.number, frameInfo)

        this.spriteTurnCache.push(sprite)
    }

    generateDisappearAnimation(frameInfo, task) {
        const to = this.getOffsetNode(task.target, task.player)
        this.performMove(frameInfo, task.player, to, to, task.amount, {}, { scaleX: 0, scaleY: 0, alpha: 0 })
    }

    performMove(frameInfo, player, from, to, amount, initialArgs = {}, finalArgs = {}) {
        // create group with sprites (or load from cache)
        if (this.knightCache.length > 0) {
            var group = this.knightCache.pop()
            var knight = group.knight
            var circle = group.circle
            var text = group.text
        } else {
            var knight = EntityFactory.create("S")
            knight.id = ++graphicsHelper.runtimeId
            entityModule.entities.set(knight.id, knight)
            TinyToggleModule.instance.registerToggle(knight, "d", false)
            var circle = EntityFactory.create("C")
            circle.id = ++graphicsHelper.runtimeId
            entityModule.entities.set(circle.id, circle)
            TinyToggleModule.instance.registerToggle(circle, "d", true)
            var text = EntityFactory.create("T")
            text.id = ++graphicsHelper.runtimeId
            entityModule.entities.set(text.id, text)
            var group = { knight: knight, circle: circle, text: text }
        }

        var t0 = initialArgs.t || 0
        var t1 = finalArgs.t || 1
        var knightOffset = 10 - 20 * player
        // set initial location
        var knightParams = { ...graphicsHelper.defaults.sprite, x: from.x + knightOffset, y: from.y, zIndex: 9, t: t0, image: "rb" [player] + Math.min(5, amount) + ".png", tint: graphicsHelper.playerColors[player], scaleX: 0.1, scaleY: 0.1, anchorX: 0.5, anchorY: 0.5 }
        knight.addState(t0, { values: knightParams, curve: {} }, frameInfo.number, frameInfo)
        var circleParams = { ...graphicsHelper.defaults.circle, x: from.x, y: from.y, t: t0, fillColor: graphicsHelper.playerColors[player], zIndex: 9 }
        circle.addState(t0, { values: circleParams, curve: {} }, frameInfo.number, frameInfo)
        var textParams = { ...graphicsHelper.defaults.text, x: from.x, y: from.y, text: "" + amount, t: t0, zIndex: 10, anchorX: 0.5, anchorY: 0.5, strokeThickness: 4, fillColor: 0xFFFFFF }
        text.addState(t0, { values: textParams, curve: {} }, frameInfo.number, frameInfo)

        // move and hide
        var knightMoveParams = { ...knightParams, x: to.x + knightOffset, y: to.y, t: t1, visible: false, ...finalArgs }
        knight.addState(t1, { values: knightMoveParams, curve: {} }, frameInfo.number, frameInfo)
        var circleMoveParams = { ...circleParams, x: to.x, y: to.y, t: t1, visible: false, ...finalArgs }
        circle.addState(t1, { values: circleMoveParams, curve: {} }, frameInfo.number, frameInfo)
        var textMoveParams = { ...textParams, x: to.x, y: to.y, t: t1, visible: false, ...finalArgs }
        text.addState(t1, { values: textMoveParams, curve: {} }, frameInfo.number, frameInfo)

        this.knightTurnCache.push(group)
    }

    handleFrameData(frameInfo, data) {
        if (!data) {
            return
        }
        const alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        var type = data[0]
        data = data.substr(1)
        var parts = data.split(';')
        for (var player = 0; player < parts.length; player++) {
            if (!parts[player]) continue
            var task = {
                player: player,
                type: type
            }
            if (type == 'M') { // move
                parts[player].match(/\w\w\d*/g).forEach(s => {
                    task.from = NodeModule.nodes[alphabet.indexOf(s[0])]
                    task.to = NodeModule.nodes[alphabet.indexOf(s[1])]
                    task.amount = 1
                    if (s.length > 2) task.amount = parseInt(s.substr(2))
                    this.generateMoveAnimation(frameInfo, task)
                })
            } else if (type == 'S') { // spawn
                parts[player].match(/\w\w\w/g).forEach(s => {
                    var node1 = NodeModule.nodes[alphabet.indexOf(s[0])]
                    var node2 = NodeModule.nodes[alphabet.indexOf(s[1])]
                    var node3 = NodeModule.nodes[alphabet.indexOf(s[2])]
                    task.from = { "x": (node1.x + node2.x + node3.x) / 3, "y": (node1.y + node2.y + node3.y) / 3 }
                    task.to = node1
                    this.generateSpawnAnimation(frameInfo, task)
                })
            } else if (type == 'X') { // surround
                parts[player].match(/\w\d*/g).forEach(s => {
                    task.target = NodeModule.nodes[alphabet.indexOf(s[0])]
                    task.amount = 1
                    if (s.length > 1) task.amount = parseInt(s.substr(1))
                    this.generateDisappearAnimation(frameInfo, task)
                })
            } else { // attack
                parts[player].match(/\w\w\w\w\d*/g).forEach(s => {
                    var node1 = NodeModule.nodes[alphabet.indexOf(s[0])]
                    var node2 = NodeModule.nodes[alphabet.indexOf(s[1])]
                    var node3 = NodeModule.nodes[alphabet.indexOf(s[2])]
                    var target = NodeModule.nodes[alphabet.indexOf(s[3])]
                    task.node1 = node1
                    task.node2 = node2
                    task.node3 = node3
                    task.triangle = { "x": (node1.x + node2.x + node3.x) / 3, "y": (node1.y + node2.y + node3.y) / 3 }
                    task.target = target
                    task.amount = 1
                    if (s.length > 4) task.amount = parseInt(s.substr(4))
                    this.generateTaskAnimation(frameInfo, task)
                })
            }
        }

        this.knightCache.push(...this.knightTurnCache)
        this.knightTurnCache = []
        this.spriteCache.push(...this.spriteTurnCache)
        this.spriteTurnCache = []
        return {}
    }

    reinitScene(container, canvasData) {}
}