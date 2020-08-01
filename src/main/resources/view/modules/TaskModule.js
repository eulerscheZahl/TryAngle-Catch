import { api as entityModule } from '../entity-module/GraphicEntityModule.js'
import { EntityFactory } from '../entity-module/EntityFactory.js'
import { TinyToggleModule } from './TinyToggleModule.js'
import { NodeModule } from './NodeModule.js'

export const graphicsHelper = {
	runtimeId: 1000000, // don't interfere with counter from SDK, as this would update the wrong entities from Java code
	playerColors: [0xff4040, 0x4040ff],
	defaults: {
		sprite: { alpha:1, anchorX:0, anchorY:0, baseHeight:null, baseWidth:null, blendMode:0, curve:{}, image:null, mask:-1, rotation:0, scaleMode:"LINEAR", scaleX:1, scaleY:1, skewX:0, skewY:0, t:0, tint:0xFFFFFF, visible:true, x:0, y:0, zIndex:0, },
		circle: { alpha: 1, blendMode: 0, curve: {}, fillAlpha: 1, fillColor: 0xFFFFFF, lineAlpha: 1, lineColor: 0, lineWidth: 1, mask: -1, radius: 25, rotation: 0, scaleX: 1, scaleY: 1, skewX: 0, skewY: 0, t: 0, visible: true, x: 0, y: 0, zIndex: 0, },
    	group: { alpha: 1, children: [], mask: -1, rotation: 0, scaleX: 1, scaleY: 1, skewX: 0, skewY: 0, visible: true, x: 0, y: 0, zIndex: 0, },
    	text: { alpha: 1, anchorX: 0, anchorY: 0, blendMode: 0, fillColor: 0, fontFamily: "Lato", fontSize: 26, fontWeight: "normal", mask: -1, maxWidth: 0, rotation: 0, scaleX: 1, scaleY: 1, skewX: 0, skewY: 0, strokeColor: 0, strokeThickness: 0, text: "", textAlign: "left", tint: 16777215, visible: true, x: 0, y: 0, zIndex: 0, },
	}
}

export class TaskModule {

  constructor (assets) {
  this.knightCache = []
  this.turnCache = []

    TaskModule.refreshContent = () => {
    }
  }

  static refreshContent () {}

  static get name () {
    return 'tasks'
  }

  updateScene (previousData, currentData, progress) {
  }

  generateMoveAnimation(frameInfo, move) {
    const offsetX = -40 + 80 * move.player
    const offsetY = -40
    const from = { x:move.from.x + offsetX, y:move.from.y + offsetY }
    const to = { x:move.to.x + offsetX, y:move.to.y + offsetY }
    this.performMove(frameInfo, move.player, from, to, move.amount)
  }

  generateSpawnAnimation(frameInfo, spawn) {
    const offsetX = -40 + 80 * spawn.player
    const offsetY = -40
    const from = { x:spawn.from.x, y:spawn.from.y }
    const to = { x:spawn.to.x + offsetX, y:spawn.to.y + offsetY }
    this.performMove(frameInfo, spawn.player, from, to, 1)
  }

  generateTaskAnimation(frameInfo, task) {
    const offsetX = -40 + 80 * task.player
    const offsetY = -40
    const to = { x:task.triangle.x + offsetX, y:task.triangle.y + offsetY }
    this.performMove(frameInfo, task.player, { x:task.node1.x + offsetX, y:task.node1.y + offsetY }, to, 1)
    this.performMove(frameInfo, task.player, { x:task.node2.x + offsetX, y:task.node2.y + offsetY }, to, 1)
    this.performMove(frameInfo, task.player, { x:task.node3.x + offsetX, y:task.node3.y + offsetY }, to, 1)
  }

  performMove(frameInfo, player, from, to, amount) {
    // create group with sprites (or load from cache)
    if (this.knightCache.length > 0) {
        var group = this.knightCache.pop()
        var knight = group.knight
        var circle = group.circle
        var text = group.text
    }
    else {
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
        var group = { knight:knight, circle:circle, text:text }
    }

    // set initial location
    var knightParams = { ...graphicsHelper.defaults.sprite, x:from.x, y:from.y, zIndex:9,
                  image:"rb"[player] + Math.min(5, amount) + ".png", tint:graphicsHelper.playerColors[player],
                  scaleX:0.1, scaleY:0.1, anchorX:0.5, anchorY:0.5 }
    knight.addState(0, {values: knightParams, curve:{}}, frameInfo.number, frameInfo)
    var circleParams = { ...graphicsHelper.defaults.circle, x:from.x, y:from.y,
                  fillColor:graphicsHelper.playerColors[player], zIndex:9 }
    circle.addState(0, {values: circleParams, curve:{}}, frameInfo.number, frameInfo)
    var textParams = { ...graphicsHelper.defaults.text, x:from.x, y:from.y, text:"1",
                  zIndex:10, anchorX:0.5, anchorY:0.5, strokeThickness:4, fillColor:0xFFFFFF }
    text.addState(0, {values: textParams, curve:{}}, frameInfo.number, frameInfo)

    // move and hide
    var knightMoveParams = { ...knightParams, x:to.x, y:to.y, t:1, visible:false }
    knight.addState(1, {values: knightMoveParams, curve:{}}, frameInfo.number, frameInfo)
    var circleMoveParams = { ...circleParams, x:to.x, y:to.y, t:1, visible:false }
    circle.addState(1, {values: circleMoveParams, curve:{}}, frameInfo.number, frameInfo)
    var textMoveParams = { ...textParams, x:to.x, y:to.y, t:1, visible:false }
    text.addState(1, {values: textMoveParams, curve:{}}, frameInfo.number, frameInfo)

    this.turnCache.push(group)
  }

  handleFrameData (frameInfo, data) {
    if (!data) {
      return
    }
    const alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    var type = data[0]
    data = data.substr(1)
    var parts = data.split(';')
    for (var player = 0; player < parts.length; player++) {
        if (!parts[player]) continue
        if (type == 'M') { // spawn
            parts[player].match(/\w\w\d*/g).forEach(s => {
                var move = {}
                move.from = NodeModule.nodes[alphabet.indexOf(s[0])]
                move.to = NodeModule.nodes[alphabet.indexOf(s[1])]
                move.amount = 1
                if (s.length > 2) move.amount = parseInt(s.substr(2))
                move.player = player;
                this.generateMoveAnimation(frameInfo, move)
            })
        } else if (type == 'S') { // spawn
            parts[player].match(/\w\w\w/g).forEach(s => {
                var spawn = {}
                var node1 = NodeModule.nodes[alphabet.indexOf(s[0])]
                var node2 = NodeModule.nodes[alphabet.indexOf(s[1])]
                var node3 = NodeModule.nodes[alphabet.indexOf(s[2])]
                spawn.from = {"x":(node1.x+node2.x+node3.x)/3, "y":(node1.y+node2.y+node3.y)/3}
                spawn.to = node1
                spawn.player = player
                this.generateSpawnAnimation(frameInfo, spawn)
            })
        } else {
            parts[player].match(/\w\w\w\w/g).forEach(s => {
                var task = {}
                var node1 = NodeModule.nodes[alphabet.indexOf(s[0])]
                var node2 = NodeModule.nodes[alphabet.indexOf(s[1])]
                var node3 = NodeModule.nodes[alphabet.indexOf(s[2])]
                var target = NodeModule.nodes[alphabet.indexOf(s[3])]
                task.node1 = node1
                task.node2 = node2
                task.node3 = node3
                task.triangle = {"x":(node1.x+node2.x+node3.x)/3, "y":(node1.y+node2.y+node3.y)/3}
                task.target = target
                task.player = player
                task.type = type
                this.generateTaskAnimation(frameInfo, task)
            })
        }
    }

    this.knightCache.push(...this.turnCache)
    this.turnCache = []
    return {}
  }

  reinitScene (container, canvasData) {
  }
}