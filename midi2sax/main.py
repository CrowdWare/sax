#import mido
from mido import MidiFile, merge_tracks

def generate_sax_svg(notes, ticks_per_beat, output_file="sax_rendered.svg"):
    BLOCK_HEIGHT = 20
    ROW_HEIGHT = 30
    PIXELS_PER_QUARTER = 160
    MIN_SPACING = 10
    X_OFFSET = 100
    PAGE_WIDTH = 1900
    LINE_HEIGHT = 240

    # Fingering-Beispiel
    note_fingerings = {
        60: [1,2,3,4,5,6,8],            # C4
        61: [1,2,3,4,5,6,7],            # C#4
        62: [1,2,3,4,5,6],              # D4
        63: [1,2,3,4,5,6,7],            # D#4
        64: [1,2,3,4,5],                # E4
        65: [1,2,3,4],                  # F4
        66: [1,2,3,5],                  # F#4
        67: [1,2,3],                    # G4
        68: [1,2,3,9],                  # G#4
        69: [1,2],                      # A4
        71: [1],                        # B4
        72: [2],                        # C5
        73: [],                         # C#5
        74: [0,1,2,3,4,5,6],            # D5
        75: [0,1,2,3,4,5,6,7],          # D#5
        76: [0,1,2,3,4,5],              # E5
        77: [0,1,2,3,4],                # F5
        78: [0,1,2,3,5],                # F#5
        79: [0,1,2,3],                  # G5
        80: [0,1,2,3,9],                # G#5 
        81: [0,1,2],                    # A6
        82: [0,1,16],                   # A#6
        83: [0,1],                      # B6
        84: [0,2],                      # C6
        85: [0],                        # C#6 
        86: [0,10],                     # D6
    }
    # fingers 9,10 und 16 do be defined

    def klappenfarbe(k):
        if k == 0: return "#E41383"
        elif 1 <= k <= 3: return "#13B2E4"
        elif 4 <= k <= 6: return "#E4D613"
        else: return "gray"

    def ticks_to_px(t): return max(2, int((t / ticks_per_beat) * PIXELS_PER_QUARTER))

    svg = []
    width = 2000
    height = 0
    max_klappe = max(k for kl in note_fingerings.values() for k in kl) if note_fingerings else 8

    svg.append(f'<svg xmlns="http://www.w3.org/2000/svg" width="{width}" height="{height}">')
    svg.append('<style> text { font-family: sans-serif; font-size: 12px; } </style>')

    x = X_OFFSET
    y_base = 0

    notes_sorted = sorted(notes, key=lambda n: n[1])
    
    for idx, (note, start, duration) in enumerate(notes_sorted):
        if note not in note_fingerings:
            print(f"⚠️  Warnung: Kein Fingering für Note {note} vorhanden – wird übersprungen.")
            continue

        if idx > 0:
            prev_note, prev_start, prev_duration = notes_sorted[idx - 1]
            pause_ticks = start - (prev_start + prev_duration)
            if pause_ticks > 0:
                pause_width = ticks_to_px(pause_ticks)
                if x + pause_width > PAGE_WIDTH:
                    x = X_OFFSET
                    y_base += LINE_HEIGHT
                
                # Pause
                x += pause_width + MIN_SPACING

        # Normale Note
        klappen = note_fingerings[note]
        w = ticks_to_px(duration)

        if x + w > PAGE_WIDTH:
            x = X_OFFSET
            y_base += LINE_HEIGHT

        for k in klappen:
            y = y_base + k * ROW_HEIGHT
            svg.append(f'<rect x="{x}" y="{y}" width="{w}" height="{BLOCK_HEIGHT}" fill="{klappenfarbe(k)}" />')

        x += w + MIN_SPACING

    svg.append('</svg>')
    
    total_lines = (y_base // LINE_HEIGHT) + 1
    total_height = total_lines * LINE_HEIGHT + (max_klappe + 1) * ROW_HEIGHT
    svg[0] = f'<svg xmlns="http://www.w3.org/2000/svg" width="{width}" height="{total_height}">'

    with open(output_file, "w") as f:
        f.write('\n'.join(svg))

    print(f"✅ SVG gespeichert: {output_file}")


def parse_midi_ticks(midi_path):
    mid = MidiFile(midi_path)
    ppq = mid.ticks_per_beat

    track = merge_tracks(mid.tracks)
    time = 0
    note_on = {}
    notes = []

    for msg in track:
        time += msg.time
        if msg.type == "note_on" and msg.velocity > 0:
            note_on[msg.note] = time
        elif msg.type in ("note_off", "note_on") and msg.note in note_on:
            start = note_on.pop(msg.note)
            duration = time - start
            if duration > 0:
                notes.append((msg.note + 9, start, duration))

    return notes, ppq

if __name__ == "__main__":
    midi_file = "Nothings_Gonna_Change_My_Love_For_you_simple.mid"
    notes, ppq = parse_midi_ticks(midi_file)
    generate_sax_svg(notes, ppq)
