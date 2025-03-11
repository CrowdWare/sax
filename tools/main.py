import mido
from fpdf import FPDF


def ascii_to_pdf(input_ascii_file, output_pdf_file, notes_per_row=23):
    with open(input_ascii_file, 'r') as f:
        lines = [line.rstrip('\n') for line in f]

    pdf = FPDF(orientation='L', format='A4')
    pdf.add_page()
    pdf.set_font('Courier', size=9)

    # Splitte alle Zeilen anhand des Trennzeichens "|"
    split_lines = [line.split('|')[:-1] for line in lines]
    total_notes = len(split_lines[0])

    start_note = 0
    while start_note < total_notes:
        end_note = min(start_note + notes_per_row, total_notes)

        for line in lines:
            notes = line.split('|')[start_note:end_note]
            segment = ' '.join(notes)
            pdf.cell(0, 4, segment, ln=True)
        pdf.ln(4)
        start_note = end_note

    pdf.output(output_pdf_file)

"""
def ascii_to_pdf(input_ascii_file, output_pdf_file, notes_per_row=14):
    with open(input_ascii_file, 'r') as f:
        lines = [line.rstrip('\n') for line in f]

    pdf = FPDF(orientation='L', format='A4')
    pdf.add_page()
    pdf.set_font('Courier', size=10)

    max_line_length = max(len(line) for line in lines)
    start_col = 0
    chars_per_note = max(len(note) for line in lines for note in line.split()) + 1
    notes_per_page = notes_per_row * chars_per_note

    while start_col < max(len(line) for line in lines):
        end_col = start_col + notes_per_page
        for line in lines:
            segment = line[start_col:end_col].rstrip()
            pdf.cell(0, 5, segment, ln=True)
        pdf.ln(4)  # kleiner Abstand zwischen Notengruppen
        start_col = end_col

    pdf.output(output_pdf_file)
     


def ascii_to_pdf(input_ascii_file, output_pdf_file, notes_per_row=8):
    with open(input_ascii_file, 'r') as f:
        lines = [line.rstrip('\n') for line in f]

    pdf = FPDF()
    pdf.add_page()
    pdf.set_font("Courier", size=10)

    # Ermittlung der Anzahl der Noten
    notes_total = len(lines[0])
    notes_processed = 0

    while notes_processed < notes_total:
        segment_end = notes_processed + notes_per_row * 17  # 16 Zeichen + 1 Leerzeichen pro Note (geschätzt)
        segment_end = min(segment_end, notes_total)

        for line in lines:
            segment = line[notes_processed:segment_end]
            pdf.cell(0, 5, segment, ln=True)

        pdf.ln(5)  # Leerzeile nach jeder Gruppe
        notes_processed = segment_end

    pdf.output(output_pdf_file)
 """
    
# Dauer in MIDI-Ticks und zugehörige Anzahl von Zeichen
durationMap = {
    120: 1,    # Sechzehntel
    240: 2,    # Achtel
    480: 4,    # Viertel
    960: 8,    # Halbe
    1920: 16   # Ganze Note
}

# MIDI-Notenwerte zu Notennamen
def midi_note_to_name(note):
    notes = ['C', 'C#', 'D', 'D#', 'E', 'F', 'F#', 'G', 'G#', 'A', 'A#', 'B']
    return notes[note % 12] + str((note // 12) - 1)

# Nächstgelegenen Wert aus der durationMap finden
def nearest_duration(ticks):
    return min(durationMap.keys(), key=lambda x: abs(x - ticks))

# Saxophon-Fingertabelle mit vertikaler Klappenstellung
sax_fingerings = {
    'C4':  [' ', 'X', 'X', 'X', 'O', 'O', 'O', 'U'],
    'D4':  [' ', 'X', 'X', 'X', 'O', 'O', 'O', ' '],
    'D#4': [' ', 'X', 'X', 'X', 'O', 'O', 'O', '^'],
    'E4':  [' ', 'X', 'X', 'X', 'O', 'O', ' ', ' '],
    'F4':  [' ', 'X', 'X', 'X', 'O', ' ', ' ', ' '],
    'G4':  [' ', 'X', 'X', 'X', ' ', ' ', ' ', ' '],
    'A4':  [' ', 'X', 'X', ' ', ' ', ' ', ' ', ' '],
    'B4':  [' ', 'X', ' ', ' ', ' ', ' ', ' ', ' '],
    'C5':  [' ', ' ', 'X', ' ', ' ', ' ', ' ', ' '],
    'D5':  ['^', 'X', 'X', 'X', 'O', 'O', 'O', ' '],
    'D#5': ['^', 'X', 'X', 'X', 'O', 'O', 'O', '^'],
    'E5':  ['^', 'X', 'X', 'X', 'O', 'O', ' ', ' '],
    'F5':  ['^', 'X', 'X', 'X', 'O', ' ', ' ', ' '],
    'G5':  ['^', 'X', 'X', 'X', ' ', ' ', ' ', ' '],
    'A5':  ['^', 'X', 'X', ' ', ' ', ' ', ' ', ' '],
    'B5':  ['^', 'X', ' ', ' ', ' ', ' ', ' ', ' '],
    'C5':  ['^', ' ', 'X', ' ', ' ', ' ', ' ', ' '],
}

"""
# MIDI verarbeiten und ASCII umwandeln (ohne Zeilenumbruch zwischen Tönen)
def midi_to_ascii(input_file, output_file):
    midi = mido.MidiFile(input_file)

    with open(output_file, 'w') as f:
        vertical_lines = ['' for _ in range(8)]

        for track in midi.tracks:
            elapsed_ticks = 0
            current_notes = {}

            for msg in track:
                elapsed_ticks += msg.time

                if msg.type == 'note_on' and msg.velocity > 0:
                    current_notes[msg.note] = elapsed_ticks
                    elapsed_ticks = 0

                elif (msg.type == 'note_off' or (msg.type == 'note_on' and msg.velocity == 0)) and msg.note in current_notes:
                    duration_ticks = elapsed_ticks
                    duration_length = nearest_duration(duration_ticks)
                    note_name = midi_note_to_name(msg.note)
                    fingering = sax_fingerings.get(note_name, [' ']*7)
                    chars = 'X' * durationMap[duration_length]

                    for i, key in enumerate(fingering):
                        if key == 'X':
                            vertical_lines[i] += chars + ' '
                        elif key in ('O', '^', 'U'):
                            vertical_lines[i] += key * len(chars) + ' '
                        else:
                            vertical_lines[i] += ' ' * len(chars) + ' '
                    elapsed_ticks = 0
                    del current_notes[msg.note]

        # Ausgabe der fertigen Zeilen
        for line in vertical_lines:
            f.write(line.rstrip() + '\n')
    """
    
def midi_to_ascii(input_file, output_file):
    midi = mido.MidiFile(input_file)

    vertical_lines = ['' for _ in range(8)]

    for track in midi.tracks:
        elapsed_ticks = 0
        current_notes = {}

        for msg in track:
            elapsed_ticks += msg.time

            if msg.type == 'note_on' and msg.velocity > 0:
                current_notes[msg.note] = elapsed_ticks
                elapsed_ticks = 0

            elif (msg.type == 'note_off' or (msg.type == 'note_on' and msg.velocity == 0)) and msg.note in current_notes:
                duration_ticks = elapsed_ticks
                nearest_tick = nearest_duration(duration_ticks)
                chars = 'X' * durationMap[nearest_tick]
                note_name = midi_note_to_name(msg.note)
                fingering = sax_fingerings.get(note_name, [' '] * 8)

                for i, key in enumerate(fingering):
                    if key == 'X':
                        vertical_lines[i] += chars
                    elif key in ('O', '^', 'U'):
                        vertical_lines[i] += key * len(chars)
                    else:
                        vertical_lines[i] += ' ' * len(chars)
                    vertical_lines[i] += '|'  # <-- Noten-Trennzeichen hinzugefügt

                elapsed_ticks = 0
                del current_notes[msg.note]

    with open(output_file, 'w') as f:
        for line in vertical_lines:
            f.write(line.rstrip('|') + '\n')  # letztes '|' entfernen
            
midi_to_ascii('hallelujah.mid', 'hallelujah.txt')
ascii_to_pdf('hallelujah.txt','hallelujah.pdf')