import { api as entityModule } from '../entity-module/GraphicEntityModule.js'
import { EntityFactory } from '../entity-module/EntityFactory.js'
import { TinyToggleModule } from './TinyToggleModule.js'
import { graphicsHelper } from './TaskModule.js'
import { FooltipModule } from './FooltipModule.js'

export class NodeModule {

  constructor (assets) {
    this.previousFrame = {}

    NodeModule.refreshContent = () => {
    }
  }

  static refreshContent () {}

  static get name () {
    return 'nodes'
  }

  updateScene (previousData, currentData, progress) {
  }

  getTooltipText(node) {
    return `House ${node.id}\nx: ${node.x}\ny: ${node.y}\nowner: ${node.owner}`
  }
  
  drawNode(frameInfo, node) {
  	if (!node.shack) {
	    node.shack = EntityFactory.create("S")
	    node.shack.id = ++graphicsHelper.runtimeId
		entityModule.entities.set(node.shack.id, node.shack)  	
	    node.roof = EntityFactory.create("S")
	    node.roof.id = ++graphicsHelper.runtimeId
		entityModule.entities.set(node.roof.id, node.roof)
	    node.baseCircle = EntityFactory.create("C")
        node.baseCircle.id = ++graphicsHelper.runtimeId
        entityModule.entities.set(node.baseCircle.id, node.baseCircle)
        node.circle = EntityFactory.create("C")
        node.circle.id = ++graphicsHelper.runtimeId
        entityModule.entities.set(node.circle.id, node.circle)
        node.text = EntityFactory.create("T")
	    node.text.id = ++graphicsHelper.runtimeId
		entityModule.entities.set(node.text.id, node.text)
  	
	  	TinyToggleModule.instance.registerToggle(node.shack, "d", false)
		TinyToggleModule.instance.registerToggle(node.roof, "d", false)
		TinyToggleModule.instance.registerToggle(node.baseCircle, "d", true)
		TinyToggleModule.instance.registerToggle(node.circle, "d", true)
		TinyToggleModule.instance.registerToggle(node.text, "d", true)
		FooltipModule.instance.registerTooltip(node.shack, this.getTooltipText(node))
		FooltipModule.instance.registerTooltip(node.circle, this.getTooltipText(node))
	}
	var shackParams = { ...graphicsHelper.defaults.sprite, x:node.x-30, y:node.y-40, zIndex:8,
				  image:"s1.png", scaleX:0.1, scaleY:0.1, }
    node.shack.addState(0, {values: shackParams, curve:{}}, frameInfo.number, frameInfo)
    
	var roofParams = { ...graphicsHelper.defaults.sprite, x:node.x-30, y:node.y-40, zIndex:8,
				  image:"s2.png", scaleX:0.1, scaleY:0.1, }
    node.roof.addState(0, {values: roofParams, curve:{}}, frameInfo.number, frameInfo)
    
	var baseCircleParams = { ...graphicsHelper.defaults.circle, x:node.x, y:node.y, zIndex:8,
				  radius:35, fillColor:0,lineWidth:10, lineColor:0  }
    node.baseCircle.addState(0, {values: baseCircleParams, curve:{}}, frameInfo.number, frameInfo)
    
	var circleParams = { ...graphicsHelper.defaults.circle, x:node.x, y:node.y, zIndex:8,
				  radius:31, fillColor:0,lineWidth:10, lineColor:0  }
    node.circle.addState(0, {values: circleParams, curve:{}}, frameInfo.number, frameInfo)

	var textParams = { ...graphicsHelper.defaults.text, x:node.x, y:node.y, zIndex:8, fontWeight: "italic",
				  text:""+node.id, strokeThickness:4, fillColor:0xffffff, anchorX:0.5, anchorY:0.5, }
    node.text.addState(0, {values: textParams, curve:{}}, frameInfo.number, frameInfo)
  }
  
  updateOwner(frameInfo, node, player) {
  	if (player == -1) {
  	    node.owner = "None"
		var roofParams = { ...graphicsHelper.defaults.sprite, x:node.x-30, y:node.y-40, zIndex:8,
					  image:"s2.png", scaleX:0.1, scaleY:0.1, t:1, }
	    node.roof.addState(1, {values: roofParams, curve:{}}, frameInfo.number, frameInfo)
   		var circleParams = { ...graphicsHelper.defaults.circle, x:node.x, y:node.y, zIndex:8,
				  radius:31, fillColor:0, lineWidth:10, lineColor:0, t:1  }
    	node.circle.addState(1, {values: circleParams, curve:{}}, frameInfo.number, frameInfo)
  	} else {
  	    node.owner = this.playerList[player].name
		var roofParams = { ...graphicsHelper.defaults.sprite, x:node.x-30, y:node.y-40, zIndex:8, t:1,
					  image:"s2.png", scaleX:0.1, scaleY:0.1, tint:graphicsHelper.playerColors[player] }
	    node.roof.addState(1, {values: roofParams, curve:{}}, frameInfo.number, frameInfo)
   		var circleParams = { ...graphicsHelper.defaults.circle, x:node.x, y:node.y, zIndex:8, t:1,
				  radius:31, fillColor:0, lineWidth:10, lineColor:graphicsHelper.playerColors[player]  }
    	node.circle.addState(1, {values: circleParams, curve:{}}, frameInfo.number, frameInfo)
  	}
    FooltipModule.instance.registerTooltip(node.shack, this.getTooltipText(node))
    FooltipModule.instance.registerTooltip(node.circle, this.getTooltipText(node))
  }
  
  updateUnits(frameInfo, node, player, time, amount) {
  	const view = node.unitView[player]
	const offsetX = -40 + 80*player
	const offsetY = -40
	const knightOffset = 10 - 20*player
  	var circleParams = { ...graphicsHelper.defaults.circle, x:node.x+offsetX, y:node.y+offsetY,
  					  fillColor:graphicsHelper.playerColors[player], zIndex:9 }
  	var knightParams = { ...graphicsHelper.defaults.sprite, x:node.x+offsetX+knightOffset, y:node.y+offsetY,
  					  tint:graphicsHelper.playerColors[player], zIndex:9,
  					  scaleX:0.1, scaleY:0.1, anchorX:0.5, anchorY:0.5 }
  	var textParams = { ...graphicsHelper.defaults.text, x:node.x+offsetX, y:node.y+offsetY,
  					  fillColor:0xffffff, strokeThickness:4, zIndex:9, anchorX:0.5, anchorY:0.5 }

	// create view if necessary
  	if (!view.circle) {
  		var knight = EntityFactory.create("S")
  		knight.id = ++graphicsHelper.runtimeId
  		entityModule.entities.set(knight.id, knight)
		TinyToggleModule.instance.registerToggle(knight, "d", false)
  		view.knight = knight
  		
  		var circle = EntityFactory.create("C")
  		circle.id = ++graphicsHelper.runtimeId
  		entityModule.entities.set(circle.id, circle)
		TinyToggleModule.instance.registerToggle(circle, "d", true)
  		view.circle = circle
  		
  		var text = EntityFactory.create("T")
  		text.id = ++graphicsHelper.runtimeId
  		entityModule.entities.set(text.id, text)
  		view.text = text

  		if (time > 0) {
            view.circle.addState(time, {values: { ...circleParams, visible:false, t:0}, curve:{}}, frameInfo.number, frameInfo)
            view.knight.addState(time, {values: { ...knightParams, visible:false, t:0, image:imageName}, curve:{}}, frameInfo.number, frameInfo)
            view.text.addState(time, {values: { ...textParams, visible:false, text:""+amount, t:0}, curve:{}}, frameInfo.number, frameInfo)
  		}
  	}

	var imageName = "rb"[player]+Math.min(amount,5)+".png"
	if (amount==0) imageName = null
  	view.circle.addState(time, {values: { ...circleParams, visible:amount>0, t:time}, curve:{}}, frameInfo.number, frameInfo)
  	view.knight.addState(time, {values: { ...knightParams, visible:amount>0, t:time, image:imageName}, curve:{}}, frameInfo.number, frameInfo)
  	view.text.addState(time, {values: { ...textParams, visible:amount>0, text:""+amount, t:time}, curve:{}}, frameInfo.number, frameInfo)
  }

  handleGlobalData(players, globalData) {
      this.playerList = players;
  }

  handleFrameData (frameInfo, data) {
    if (!data) {
      return
    }
    var newRegistration = {}
    var nodeId = 0
    const alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    data.split(' ').forEach(s => {
        if (s.includes("/")) {
            const xy = s.split('/')
            const x = +xy[0]
            const y = +xy[1]
            const node = {"id":nodeId++, "x":x, "y":y, owner:"None", "unitView":{0:{},1:{}}}
            newRegistration[node.id] = node
            NodeModule.nodes[node.id] = node
            this.drawNode(frameInfo, node)
        }
        else if (s.includes("=")) {
        	const player = +s[0]
        	const time = +s[1]
        	s.substring(3).match(/\w\d*/g).forEach(p => {
    			const nodeId = alphabet.indexOf(p[0])
    			const amount = +p.substr(1) || time
    			this.updateUnits(frameInfo, NodeModule.nodes[nodeId], player, time, amount)
        	})
        }
        else if (s[0] == 'X') {
        	var player = -1
        	for (var i = 1; i < s.length; i++) {
        		const c = s[i]
        		if (c == '0') player = 0
        		else if (c == '1') player = 1
        		else this.updateOwner(frameInfo, NodeModule.nodes[alphabet.indexOf(c)], player)
        	}
        }
    })
    const registered = { ...this.previousFrame.registered, ...newRegistration }
    const frame = { registered, number: frameInfo.number }
    this.previousFrame = frame
    return frame
  }

  reinitScene (container, canvasData) {
    NodeModule.refreshContent()
  }
}

NodeModule.nodes = {}