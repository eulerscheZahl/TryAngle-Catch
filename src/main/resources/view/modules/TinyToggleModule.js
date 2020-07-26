import { api as entityModule } from '../entity-module/GraphicEntityModule.js'
import { ErrorLog } from '../core/ErrorLog.js'
import { MissingToggleError } from '../toggle-module/errors/MissingToggleError.js'
import { DuplicateToggleValueError } from '../toggle-module/errors/DuplicateToggleValueError.js'

export class TinyToggleModule {
  constructor (assets) {
    this.previousFrame = {}
    this.missingToggles = {}

    TinyToggleModule.refreshContent = () => {
      if (!this.currentFrame) {
        return
      }
      for (const registeredEntity in this.currentFrame.registered) {
        const entity = entityModule.entities.get(parseInt(registeredEntity))
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
    }

    pushDuplicateErrors()
  }

  static refreshContent () {}

  static defineToggle (option) {
    checkDuplicates(option)

    TinyToggleModule.toggles[option.toggle] = option.default
    option.get = () => TinyToggleModule.toggles[option.toggle]
    option.set = (value) => {
      TinyToggleModule.toggles[option.toggle] = value
      TinyToggleModule.refreshContent()
    }
    return option
  }

  static get name () {
    return 'toggles'
  }

  updateScene (previousData, currentData, progress) {
    this.currentFrame = currentData
    this.currentProgress = progress
    TinyToggleModule.refreshContent()
  }

  handleFrameData (frameInfo, data) {
    if (!data) {
      return
    }
    var newRegistration = {}
    Object.entries(data).forEach(([key, value]) => {
        value.match(/\d+./g).forEach(m => {
            var entityId = m.slice(0, -1)
            var state = m.slice(-1) === "+"
            newRegistration[entityId] = {"name":key, "state":state}
        })
    })
    const registered = { ...this.previousFrame.registered, ...newRegistration }
    const frame = { registered, number: frameInfo.number }
    this.previousFrame = frame
    return frame
  }

  reinitScene (container, canvasData) {
    TinyToggleModule.refreshContent()
  }
}

TinyToggleModule.toggles = {}
TinyToggleModule.optionValues = {}

function checkDuplicates (option) {
  TinyToggleModule.optionValues[option.toggle] = []

  for (const key in option.values) {
    const value = option.values[key]
    let matchedPair = TinyToggleModule.optionValues[option.toggle].find(elem => elem.value === value)

    if (!matchedPair) {
      matchedPair = { keys: [ ], value }
      TinyToggleModule.optionValues[option.toggle].push(matchedPair)
    }

    matchedPair.keys.push(key)
  }
}

function pushDuplicateErrors () {
  for (const toggle in TinyToggleModule.optionValues) {
    for (const optionValues of TinyToggleModule.optionValues[toggle]) {
      if (optionValues.keys.length > 1) {
        ErrorLog.push(new DuplicateToggleValueError(toggle, optionValues))
      }
    }
  }
}