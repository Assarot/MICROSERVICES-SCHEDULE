import csv
from openpyxl import Workbook

wb = Workbook()
# Hoja 1: Datos Básicos
ws1 = wb.active
ws1.title = "Datos Basicos"
ws1.append(["Estado", "EstadoActivo", "TipoAcademico", "TipoActivo", "Edificio", "EdificioActivo", "Piso", "PisoActivo", "EdificioPiso"])

# Basic Data
ws1.append(["Disponible", "A", "Aula", "A", "Pabellón A", "A", 1, "A", "Pabellón A"])
ws1.append(["Ocupado", "A", "Laboratorio", "A", "Pabellón B", "A", 2, "A", "Pabellón A"])
ws1.append(["Mantenimiento", "A", "Taller", "A", "Pabellón C", "A", 3, "A", "Pabellón A"])
ws1.append(["", "", "Laboratorio de Redes", "A", "", "", 1, "A", "Pabellón B"])
ws1.append(["", "", "Laboratorio de Software", "A", "", "", 2, "A", "Pabellón B"])
ws1.append(["", "", "Aula Magna", "A", "", "", 3, "A", "Pabellón B"])
ws1.append(["", "", "Laboratorio de Cómputo", "A", "", "", 1, "A", "Pabellón C"])
ws1.append(["", "", "Biblioteca", "A", "", "", 2, "A", "Pabellón C"])
ws1.append(["", "", "Auditorio", "A", "", "", "", "", ""])
ws1.append(["", "", "Gimnasio", "A", "", "", "", "", ""])

# Hoja 2: Ambientes
ws2 = wb.create_sheet("Ambientes")
ws2.append(["Espacio", "Observacion", "Ubicacion", "Capacidad", "Estado", "Tipo", "Edificio", "Piso"])

try:
    with open('ambientes_academicos_prueba.csv', 'r', encoding='utf-8') as f:
        reader = csv.reader(f)
        next(reader) # skip header
        for row in reader:
            # Reemplazar posibles fallos de codificación
            row = [c.replace('', 'ó') for c in row] 
            ws2.append(row)
except Exception as e:
    print(e)
    pass

wb.save("ambientes.xlsx")
print("Excel file 'ambientes.xlsx' generated successfully.")
