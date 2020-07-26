import { EndScreenModule } from './endscreen-module/EndScreenModule.js';
import { GraphicEntityModule } from './entity-module/GraphicEntityModule.js';
import { TinyToggleModule } from './modules/TinyToggleModule.js'
import { TooltipModule } from './tooltip-module/TooltipModule.js'

// List of viewer modules that you want to use in your game
export const modules = [
    EndScreenModule,
	GraphicEntityModule,
	TinyToggleModule,
	TooltipModule,
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