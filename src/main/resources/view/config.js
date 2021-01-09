import { GraphicEntityModule } from './entity-module/GraphicEntityModule.js';
import { TinyToggleModule } from './modules/TinyToggleModule.js'
import { NodeModule } from './modules/NodeModule.js'
import { TaskModule } from './modules/TaskModule.js'
import { FooltipModule } from './modules/FooltipModule.js'
import { EndScreenModule } from './endscreen-module/EndScreenModule.js';

export const playerColors = [
  '#FF1D5C', //'#ff4040',
  '#22A1E4', //'#4040ff',
];

// List of viewer modules that you want to use in your game
export const modules = [
	GraphicEntityModule,
	TinyToggleModule,
	FooltipModule,
	NodeModule,
	TaskModule,
    EndScreenModule,
];

// The list of toggles displayed in the options of the viewer
export const options = [
  TinyToggleModule.defineToggle({
    toggle: 'd',
    title: 'DEBUG',
    values: {
      'DEBUG ON': true,
      'DEBUG OFF': false
    },
    default: false
  }),
  TinyToggleModule.defineToggle({
    toggle: 'myColor',
    title: 'ASSIGN STATIC COLOR TO ME',
    values: {
      'NO': 0,
      'RED': 1,
      'BLUE': 2,
      'GREEN': 3,
      'YELLOW': 4,
      'PURPLE': 5,
    },
    default: 0
  }),
  TinyToggleModule.defineToggle({
    toggle: 'oppColor',
    title: 'ASSIGN STATIC COLOR TO OPPONENT',
    values: {
      'NO': 0,
      'RED': 1,
      'BLUE': 2,
      'GREEN': 3,
      'YELLOW': 4,
      'PURPLE': 5,
    },
    default: 0
  }),
]