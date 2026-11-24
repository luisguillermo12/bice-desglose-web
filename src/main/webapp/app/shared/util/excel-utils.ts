import { Injectable } from '@angular/core';
import { IConcepto } from 'app/shared/model/concepto.model';

@Injectable({ providedIn: 'root' })
export class ExcelUtils {
    public convertToExcel(grilla, varConcepto: IConcepto, OPC: string, numPeriodo): string {
        let formula = '';
        /*Dim formula, frmlExcel, tipoConcepto, codConcepto, Aux As String
        Dim posOpen, posClose, posPoint, posOpenAux, Row, numAux, modifPeriodoDestino, Index As Integer
        Dim sColumns, Col As String
        Dim flgError As Boolean

        If OPC = "PRC" Then
        formula = varConcepto.formulaPRC
        ElseIf OPC = "VAL" Then
        If grilla = 2 And varConcepto.formulaVAL <> "" And InStr(1, varConcepto.formulaDEF, "[" & glsVar_Me & "]") <> 0 Then
        formula = Replace(varConcepto.formulaDEF, "[" & glsVar_Me & "]", "(" & varConcepto.formulaVAL & ")")
        Else
        formula = varConcepto.formulaVAL
        End If
        ElseIf OPC = "DEF" Then
        formula = varConcepto.formulaDEF
        End If

        modifPeriodoDestino = 0
        flgError = False
        frmlExcel = ""
        sColumns = "ABCDEFGHIJ"
        posOpen = InStr(1, formula, "[")
        If OPC = "DEF" Then
        posOpenAux = InStr(1, formula, "[" & glsVar_Me & "]")
        While posOpenAux = posOpen And posOpen <> 0
        posOpen = InStr(posOpen + Len("[" & glsVar_Me & "]"), formula, "[")
        posOpenAux = InStr(posOpenAux + Len("[" & glsVar_Me & "]"), formula, "[" & glsVar_Me & "]")
        Wend
        End If

        While posOpen <> 0 And Not flgError
        posClose = InStr(posOpen + 1, formula, "]")
        If posClose <> 0 Then
        frmlExcel = frmlExcel & Mid(formula, 1, posOpen - 1)
        Aux = Mid(formula, posOpen + 1, (posClose - posOpen) - 1)
        formula = Mid(formula, posClose + 1)

        'Verifica que el periodo referenciado sea el siguiente o el anterior
        modifPeriodoDestino = 0
        If InStr(1, Aux, "P_SIG.") > 0 Then
        modifPeriodoDestino = 1
        Aux = Mid(Aux, InStr(1, Aux, "P_SIG.") + Len("P_SIG."))
        ElseIf InStr(1, Aux, "P_ANT.") > 0 Then
        modifPeriodoDestino = -1
        Aux = Mid(Aux, InStr(1, Aux, "P_ANT.") + Len("P_ANT."))
        End If

        If Aux = glsVar_DeflactorBase Then
        frmlExcel = frmlExcel & DeflactorBase
        ElseIf Aux = glsVar_IVA Then
        frmlExcel = frmlExcel & Valor_IVA
        ElseIf Aux = glsVar_DeflactorPeriodo Then
        numAux = numPeriodo + modifPeriodoDestino
        If numAux = 0 Then
        'Periodo Anterior al primero de la pantalla
        frmlExcel = frmlExcel & DeflactorPeriodo_Ant
        ElseIf numAux = 1 Then
        frmlExcel = frmlExcel & DeflactorPeriodo1
        ElseIf numAux = 2 Then
        frmlExcel = frmlExcel & DeflactorPeriodo2
        ElseIf numAux = 3 Then
        frmlExcel = frmlExcel & DeflactorPeriodo3
        ElseIf numAux = 4 Then
        frmlExcel = frmlExcel & DeflactorPeriodo4
        ElseIf numAux = 5 Then
        'Periodo Siguiente al último de la pantalla
        frmlExcel = frmlExcel & DeflactorPeriodo_Sig
        Else
        flgError = True
        End If
        ElseIf Aux = glsVar_NumMeses Then
        numAux = numPeriodo + modifPeriodoDestino
        If numAux = 0 Then
        'Periodo Anterior al primero de la pantalla
        frmlExcel = frmlExcel & NumMeses_Ant
        ElseIf numAux = 1 Then
        frmlExcel = frmlExcel & NumMeses1
        ElseIf numAux = 2 Then
        frmlExcel = frmlExcel & NumMeses2
        ElseIf numAux = 3 Then
        frmlExcel = frmlExcel & NumMeses3
        ElseIf numAux = 4 Then
        frmlExcel = frmlExcel & NumMeses4
        ElseIf numAux = 5 Then
        'Periodo Siguiente al último de la pantalla
        frmlExcel = frmlExcel & NumMeses_Sig
        Else
        flgError = True
        End If
        Else
        'Asigna el Tipo de Concepto de la Referencia en la Formula
        If InStr(1, Aux, "BALANCE.") > 0 Then
        tipoConcepto = "BALANCE"
        Aux = Mid(Aux, InStr(1, Aux, "BALANCE.") + Len("BALANCE."))
        ElseIf InStr(1, Aux, "EST_RESULT.") > 0 Then
        tipoConcepto = "EST_RESULT"
        Aux = Mid(Aux, InStr(1, Aux, "EST_RESULT.") + Len("EST_RESULT."))
        ElseIf InStr(1, Aux, "GEN_FONDOS.") > 0 Then
        tipoConcepto = "GEN_FONDOS"
        Aux = Mid(Aux, InStr(1, Aux, "GEN_FONDOS.") + Len("GEN_FONDOS."))
        ElseIf InStr(1, Aux, "IND_FINAN.") > 0 Then
        tipoConcepto = "IND_FINAN"
        Aux = Mid(Aux, InStr(1, Aux, "IND_FINAN.") + Len("IND_FINAN."))
        Else
        tipoConcepto = varConcepto.tipoConcepto
        End If

        'Asigna el Código de Concepto referenciado en la formula
        codConcepto = Aux

        Row = getConceptRow(grilla, tipoConcepto, codConcepto)

        If Row <> -1 Then
        numAux = (numPeriodo - 1) + modifPeriodoDestino
        If numAux = -1 Then
        'Enlazar a Periodo Anterior
        Col = COL_PRD_ANTERIOR
        frmlExcel = frmlExcel & Col & CStr(Row)
        ElseIf numAux = 4 Then
        'Enlazar a Periodo Siguiente
        Col = COL_PRD_SIGUIENTE
        frmlExcel = frmlExcel & Col & CStr(Row)
        ElseIf numAux >= 0 And numAux <= 3 Then
        'Periodo en pantalla, normal
        numAux = 3 + (numAux * 2)
        Col = Mid(sColumns, numAux, 1)
        frmlExcel = frmlExcel & Col & CStr(Row)
        Else
        flgError = True
        End If
        Else
        flgError = True
        End If
        End If

        posOpen = InStr(1, formula, "[")
        If OPC = "DEF" Then
        posOpenAux = InStr(1, formula, "[" & glsVar_Me & "]")
        While posOpenAux = posOpen And posOpen <> 0
        posOpen = InStr(posOpen + Len("[" & glsVar_Me & "]"), formula, "[")
        posOpenAux = InStr(posOpenAux + Len("[" & glsVar_Me & "]"), formula, "[" & glsVar_Me & "]")
        Wend
        End If
        Else
        flgError = True
        End If
        Wend

        frmlExcel = frmlExcel & formula

        If (tipoConcepto = "BALANCE" And varConcepto.formulaVAL <> "" And numPeriodo = 3) Then
        LOG 3, "frmlExcel [" & frmlExcel & "] varConcepto.formulaVAL[" & varConcepto.formulaVAL & "] numPeriodo[" & numPeriodo & "] posOpen[" & posOpen & "] posClose[" & posClose & "] glsConcepto[" & varConcepto.glsConcepto & "]"
        End If

        If Not flgError Then
        convertToExcel = frmlExcel
        Else
        convertToExcel = ""
        End If
*/
        return formula;
    }

}
