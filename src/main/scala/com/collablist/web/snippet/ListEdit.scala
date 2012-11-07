package com.collablist.web.snippet

import net.liftweb.util.Helpers._
import com.collablist.web.model.ListEntry
import net.liftweb.common.{Logger, Full}
import bootstrap.liftweb.Boot
import net.liftweb.http.S
import xml.NodeSeq

class ListEdit {
  val logger = Logger(classOf[Boot])

  def list = ListEntry.findAll().map(("#entry *" #> _.asHtml))
  def form(seq : NodeSeq )= {
     logger.info("test");

    val todo = ListEntry.create

    def checkAndSave(): Unit =
      todo.validate match {
        case Nil => todo.save ; S.notice("Added "+todo.text.is)
        case xs => S.error(xs) ; S.mapSnippet("ListEdit.form", doBind)
      }

    def doBind(seq: NodeSeq) = todo.toForm(Full("Save"), checkAndSave)

    doBind(seq)
  }

}

