package com.collablist.web.model{

import net.liftweb.mapper._

class ListEntry extends LongKeyedMapper[ListEntry] with IdPK {
  def getSingleton = ListEntry
  object text extends MappedString(this,120)
}

object ListEntry extends ListEntry with LongKeyedMetaMapper[ListEntry]{
  override def dbTableName = "entries"
  override def fieldOrder = List(text)

}

}

