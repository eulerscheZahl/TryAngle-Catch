import {
    api as entityModule
} from '../entity-module/GraphicEntityModule.js'
import {
    ErrorLog
} from '../core/ErrorLog.js'
import {
    MissingToggleError
} from '../toggle-module/errors/MissingToggleError.js'
import {
    DuplicateToggleValueError
} from '../toggle-module/errors/DuplicateToggleValueError.js'
import {
    FooltipModule
} from './FooltipModule.js'

export class TinyToggleModule {
    constructor(assets) {
        this.previousFrame = {}
        this.missingToggles = {}
        TinyToggleModule.instance = this

        TinyToggleModule.refreshContent = () => {
            if (!this.currentFrame) {
                return
            }
            for (const registeredEntity in this.currentFrame.registered) {
                const entity = entityModule.entities.get(parseInt(registeredEntity))
                if (!entity) continue
                const toggleInfo = this.currentFrame.registered[registeredEntity]
                const toggleState = TinyToggleModule.toggles[toggleInfo.name]

                if (toggleState == null && !this.missingToggles[toggleInfo.name]) {
                    ErrorLog.push(new MissingToggleError(toggleInfo.name))
                    this.missingToggles[toggleInfo.name] = true
                }
                entity.setHidden(
                    toggleState !== toggleInfo.state
                )
            }

            if (TinyToggleModule.toggles["color"]) TinyToggleModule.replaceColor(TinyToggleModule.toggleColor, [0x40ff40, 0x40dd40]);
            else TinyToggleModule.replaceColor([0x40ff40, 0x40dd40], TinyToggleModule.toggleColor);
        }

        pushDuplicateErrors()
    }

    registerToggle(entity, name, state) {
        this.previousFrame.registered[entity.id] = {
            "name": name,
            "state": state
        }
    }

    static refreshContent() {}

    static toRgb(color) {
        return "rgb(" + (color>>16) + ", " + ((color>>8) & 0xff) + ", " + (color&0xff) + ")";
    }

    static replaceColor(oldColor, newColor) {
        entityModule.entities.forEach(e => {
            for (var stateKey in e.states) {
                var frameStates = e.states[stateKey]
                frameStates.forEach(s => {
                    if (s.fillColor == oldColor[0]) s.fillColor = newColor[0];
                    if (s.lineColor == oldColor[0]) s.lineColor = newColor[0];
                    if (s.tint == oldColor[0]) s.tint = newColor[0];
                })
            }
        })

        var markerContainer = document.getElementsByClassName("marker_container")[0]
        var subClasses = ["ai_player", "tooltip_content", "tooltipFrameTriangle"]
        subClasses.forEach(c => {
            markerContainer.getElementsByClassName(c).forEach(e => {
                if (e.style.backgroundColor == TinyToggleModule.toRgb(oldColor[1])) e.style.backgroundColor = TinyToggleModule.toRgb(newColor[1]);
                if (e.style.borderTopColor == TinyToggleModule.toRgb(oldColor[1])) e.style.borderTopColor = TinyToggleModule.toRgb(newColor[1]);
            })
        })
    }

    static defineToggle(option) {
        checkDuplicates(option)

        TinyToggleModule.toggles[option.toggle] = option.default
        option.get = () => TinyToggleModule.toggles[option.toggle]
        option.set = (value) => {
            TinyToggleModule.toggles[option.toggle] = value
            TinyToggleModule.refreshContent()
            FooltipModule.instance.resetTooltip()
        }
        return option
    }

    static get name() {
        return 'toggles'
    }

    updateScene(previousData, currentData, progress) {
        this.currentFrame = currentData
        this.currentProgress = progress
        TinyToggleModule.refreshContent()
    }

    handleFrameData(frameInfo, data) {
        var newRegistration = {}
        if (data) {
            Object.entries(data).forEach(([key, value]) => {
                value.match(/\d+./g).forEach(m => {
                    var entityId = m.slice(0, -1)
                    var state = m.slice(-1) === "+"
                    newRegistration[entityId] = {
                        "name": key,
                        "state": state
                    }
                })
            })
        }
        const registered = {
            ...this.previousFrame.registered,
            ...newRegistration
        }
        const frame = {
            registered,
            number: frameInfo.number
        }
        this.previousFrame = frame
        return frame
    }

    handleGlobalData(players, globalData) {
        TinyToggleModule.toggleColor = -1;
        //players[0].isMe = true;
        if (players[0].isMe && !players[1].isMe) TinyToggleModule.toggleColor = [0xff4040, players[0].color];
        if (players[1].isMe && !players[0].isMe) TinyToggleModule.toggleColor = [0x4040ff, players[1].color];
    }

    reinitScene(container, canvasData) {
        TinyToggleModule.refreshContent()
    }
}

TinyToggleModule.instance = null;
TinyToggleModule.toggles = {}
TinyToggleModule.optionValues = {}

function checkDuplicates(option) {
    TinyToggleModule.optionValues[option.toggle] = []

    for (const key in option.values) {
        const value = option.values[key]
        let matchedPair = TinyToggleModule.optionValues[option.toggle].find(elem => elem.value === value)

        if (!matchedPair) {
            matchedPair = {
                keys: [],
                value
            }
            TinyToggleModule.optionValues[option.toggle].push(matchedPair)
        }

        matchedPair.keys.push(key)
    }
}

function pushDuplicateErrors() {
    for (const toggle in TinyToggleModule.optionValues) {
        for (const optionValues of TinyToggleModule.optionValues[toggle]) {
            if (optionValues.keys.length > 1) {
                ErrorLog.push(new DuplicateToggleValueError(toggle, optionValues))
            }
        }
    }
}