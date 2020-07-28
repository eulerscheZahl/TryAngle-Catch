export class NodeModule {

  constructor (assets) {
    this.previousFrame = {}
    this.missingToggles = {}

    NodeModule.refreshContent = () => {
    }
  }

  static refreshContent () {}

  static get name () {
    return 'nodes'
  }

  updateScene (previousData, currentData, progress) {
  }

  handleFrameData (frameInfo, data) {
    if (!data) {
      return
    }
    var newRegistration = {}
    data.split(' ').forEach(s => {
        if (s.includes("/")) {
            const xy = s.split('/')
            const x = +xy[0]
            const y = +xy[1]
            const node = {"x":x, "y":y}
            const id = Object.keys(NodeModule.nodes).length
            newRegistration[id] = node
            NodeModule.nodes[id] = node
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