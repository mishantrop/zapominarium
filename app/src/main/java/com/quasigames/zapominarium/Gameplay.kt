package com.quasigames.zapominarium

class Gameplay {
  var field: Field? = null

  fun init(field: Field) {
    this.field = field
  }

  fun decreaseComplexity() {
    var width = field?.width
    var height = field?.height

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

    field?.setSize(width, height)
  }

  fun increaseComplexity() {
    var width = field?.width!!
    var height = field?.height!!
    var maxWidth = field?.maxWidth
    var maxHeight = field?.maxHeight

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

    field?.setSize(width, height!!)
  }
}
