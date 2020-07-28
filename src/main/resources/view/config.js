import { GraphicEntityModule } from './entity-module/GraphicEntityModule.js';
import { TinyToggleModule } from './modules/TinyToggleModule.js'
import { NodeModule } from './modules/NodeModule.js'
import { MoveModule } from './modules/MoveModule.js'
import { TooltipModule } from './tooltip-module/TooltipModule.js'
import { EndScreenModule } from './endscreen-module/EndScreenModule.js';

export const playerColors = [
  '#ff4040',
  '#4040ff',
];

export const graphicsHelper = {
	runtimeId: 1000000, // don't interfere with counter from SDK, as this would update the wrong entities from Java code
	playerColors: [0xff4040, 0x4040ff],
	defaults: {
		sprite: { alpha:1, anchorX:0, anchorY:0, baseHeight:null, baseWidth:null, blendMode:0, curve:{}, image:null, mask:-1, rotation:0, scaleMode:"LINEAR", scaleX:1, scaleY:1, skewX:0, skewY:0, t:0, tint:0xFFFFFF, visible:true, x:0, y:0, zIndex:0, },
		circle: { alpha: 1, blendMode: 0, curve: {}, fillAlpha: 1, fillColor: 0xFFFFFF, lineAlpha: 1, lineColor: 0, lineWidth: 1, mask: -1, radius: 25, rotation: 0, scaleX: 1, scaleY: 1, skewX: 0, skewY: 0, t: 0, visible: true, x: 0, y: 0, zIndex: 0, },
    	group: { children:[], visible:true, x:0, y:0, zIndex:0, maskId:-1, },
    	text: { alpha: 1, anchorX: 0, anchorY: 0, blendMode: 0, fillColor: 0, fontFamily: "Lato", fontSize: 26, fontWeight: "normal", mask: -1, maxWidth: 0, rotation: 0, scaleX: 1, scaleY: 1, skewX: 0, skewY: 0, strokeColor: 0, strokeThickness: 0, text: "", textAlign: "left", tint: 16777215, visible: true, x: 0, y: 0, zIndex: 0, },
	}	
}

// List of viewer modules that you want to use in your game
export const modules = [
	GraphicEntityModule,
	TinyToggleModule,
	TooltipModule,
	NodeModule,
	MoveModule,
    EndScreenModule,
];

// The list of toggles displayed in the options of the viewer
export const options = [
  TinyToggleModule.defineToggle({
    // The name of the toggle
    // replace "myToggle" by the name of the toggle you want to use
    toggle: 'd',
    // The text displayed over the toggle
    title: 'DEBUG',
    // The labels for the on/off states of your toggle
    values: {
      'DEBUG ON': true,
      'DEBUG OFF': false
    },
    // Default value of your toggle
    default: false
  }),
]