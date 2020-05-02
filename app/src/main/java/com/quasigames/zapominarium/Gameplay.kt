package com.quasigames.zapominarium

class Gameplay {
  var field: Field? = null

  fun init(field: Field) {
    this.field = field
  }

  fun decreaseComplexity() {
    var width = this.field?.getWidth()
    var height = this.field?.getHeight()

    if (width!! % 2 != 0) {
      width--
    } else if (height!! % 2 != 0) {
      height--
    } else if (height > width) {
      height--
    } else {
      width--
    }

    if (width <= 0 || height!! <= 0) {
      return
    }

    this.field?.setSize(width, height)
  }

  fun increaseComplexity() {
    var width = this.field?.getWidth()
    var height = this.field?.getHeight()
    var maxWidth = this.field?.getMaxWidth()
    var maxHeight = this.field?.getMaxHeight()

    if (width!! % 2 != 0 && width!! < maxWidth!!) {
      width++
    } else if (height!! % 2 != 0 && height < maxHeight!!) {
      height++
    } else if (height!! > width) {
      width++
    } else {
      height++
    }

    if (width > maxWidth!! || height!! > maxHeight!!) {
      return
    }

    this.field?.setSize(width, height!!)
  }
}
