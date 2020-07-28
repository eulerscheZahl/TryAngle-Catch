import { api as entityModule } from '../entity-module/GraphicEntityModule.js'
import { EntityFactory } from '../entity-module/EntityFactory.js'
import { TinyToggleModule } from './TinyToggleModule.js'
import { NodeModule } from './NodeModule.js'
import { graphicsHelper } from '../config.js'

export class MoveModule {

  constructor (assets) {
  this.knightCache = []
  this.circleCache = []
  this.textCache = []

    MoveModule.refreshContent = () => {
    }
  }

  static refreshContent () {}

  static get name () {
    return 'moves'
  }

  updateScene (previousData, currentData, progress) {
  }
  
  generateMoveAnimation(frameInfo, moves) {
  	var turnKnightCache = []
  	var turnCircleCache = []
  	var turnTextCache = []
  	moves.forEach(m => {
  		const offsetX = -40 + 80*m.player
  		const offsetY = -40
  		// create group with sprites (or load from cache)
  		if (this.knightCache.length > 0) var knight = this.knightCache.pop()
  		else {
	  		var knight = EntityFactory.create("S")
	  		knight.id = ++graphicsHelper.runtimeId
	  		entityModule.entities.set(knight.id, knight)
  			TinyToggleModule.instance.registerToggle(knight, "d", false)
  		}
  		if (this.circleCache.length > 0) var circle = this.circleCache.pop()
  		else {
	  		var circle = EntityFactory.create("C")
	  		circle.id = ++graphicsHelper.runtimeId
	  		entityModule.entities.set(circle.id, circle)
  			TinyToggleModule.instance.registerToggle(circle, "d", true)
  		}
  		if (this.textCache.length > 0) var text = this.textCache.pop()
  		else {
	  		var text = EntityFactory.create("T")
	  		text.id = ++graphicsHelper.runtimeId
	  		entityModule.entities.set(text.id, text)
  		}
  		
  		// set initial location
  		var knightParams = { ...graphicsHelper.defaults.sprite, x:m.from.x+offsetX, y:m.from.y+offsetY, zIndex:9,
  					  image:"rb"[m.player] + Math.min(m.count, 5) + ".png", tint:graphicsHelper.playerColors[m.player],
  					  scaleX:0.1, scaleY:0.1, anchorX:0.5, anchorY:0.5 }
  		knight.addState(0, {values: knightParams, curve:{}}, frameInfo.number, frameInfo)
  		var circleParams = { ...graphicsHelper.defaults.circle, x:m.from.x+offsetX, y:m.from.y+offsetY,
  					  fillColor:graphicsHelper.playerColors[m.player], zIndex:9 }
  		circle.addState(0, {values: circleParams, curve:{}}, frameInfo.number, frameInfo)
  		var textParams = { ...graphicsHelper.defaults.text, x:m.from.x+offsetX, y:m.from.y+offsetY, text:""+m.count,
  					  zIndex:10, anchorX:0.5, anchorY:0.5, strokeThickness:4, fillColor:0xFFFFFF }
  		text.addState(0, {values: textParams, curve:{}}, frameInfo.number, frameInfo)
  		
  		// move and hide
  		var knightMoveParams = { ...knightParams, x:m.to.x + offsetX, y:m.to.y + offsetY, t:1, visible:false }
  		knight.addState(1, {values: knightMoveParams, curve:{}}, frameInfo.number, frameInfo)
  		var circleMoveParams = { ...circleParams, x:m.to.x + offsetX, y:m.to.y + offsetY, t:1, visible:false }
  		circle.addState(1, {values: circleMoveParams, curve:{}}, frameInfo.number, frameInfo)
  		var textMoveParams = { ...textParams, x:m.to.x + offsetX, y:m.to.y + offsetY, t:1, visible:false }
  		text.addState(1, {values: textMoveParams, curve:{}}, frameInfo.number, frameInfo)
  		
  		turnKnightCache.push(knight)
  		turnCircleCache.push(circle)
  		turnTextCache.push(text)
  	})
  	this.knightCache.push(...turnKnightCache)
  	this.circleCache.push(...turnCircleCache)
  	this.textCache.push(...turnTextCache)
  }

  handleFrameData (frameInfo, data) {
    if (!data) {
      return
    }
    var newRegistration = []
    const alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    for (var player = 0; player <= 1; player++) {
    if (data[player] === "") continue
    	data[player].match(/\w\w\d*/g).forEach(s => {
    		var move = {}
    		move.from = NodeModule.nodes[alphabet.indexOf(s[0])]
    		move.to = NodeModule.nodes[alphabet.indexOf(s[1])]
    		move.count = 1
    		if (s.length > 2) move.count = parseInt(s.substr(2))
    		move.player = player;
    		newRegistration.push(move)
    	})
    }
    this.generateMoveAnimation(frameInfo, newRegistration)
    return {} // { ...frameInfo }
  }

  reinitScene (container, canvasData) {
  }
}
