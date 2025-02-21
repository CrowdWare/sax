import mido
import matplotlib.pyplot as plt

from mido import Message, MidiFile, MidiTrack

def create_hallelujah_midi(filename="hallelujah.mid"):
    # MIDI-Datei und Track erstellen
    midi = MidiFile()
    track = MidiTrack()
    midi.tracks.append(track)

    # Instrument setzen (Altsaxophon, General MIDI Program 65)
    track.append(Message('program_change', program=65, time=0))

    # "Hallelujah" Melodie (vereinfachte Version in C-Dur)
    # (Note, Duration in ticks)
    melody = [
        (67, 480), (67, 480), (69, 480), (67, 480), (64, 960), (65, 960),  # "Now I've heard there was a secret chord"
        (67, 480), (67, 480), (69, 480), (67, 480), (72, 960), (71, 960),  # "That David played, and it pleased the Lord"
        (67, 480), (67, 480), (69, 480), (67, 480), (64, 960), (65, 960),  # "But you don't really care for music, do you?"
        (67, 480), (67, 480), (69, 480), (67, 480), (72, 960), (71, 960),  # "It goes like this, the fourth, the fifth"
        (72, 480), (71, 480), (69, 480), (67, 480), (64, 960), (64, 960),  # "The minor falls, the major lifts"
        (67, 480), (67, 480), (69, 480), (67, 480), (64, 960), (65, 960),  # "The baffled king composing Hallelujah"
    ]

    # Melodie in den Track einfügen
    for note, duration in melody:
        track.append(Message('note_on', note=note, velocity=64, time=0))
        track.append(Message('note_off', note=note, velocity=64, time=duration))

    # MIDI-Datei speichern
    midi.save(filename)
    print(f"MIDI file '{filename}' created successfully.")


def midi_to_bars(midi_file, output_file="output.png"):
    # MIDI-Datei einlesen
    midi = mido.MidiFile(midi_file)
    notes = []
    
    current_time = 0
    for msg in midi:
        current_time += msg.time
        if msg.type == 'note_on' and msg.velocity > 0:
            notes.append({
                'note': msg.note,
                'start': current_time,
                'duration': msg.time if msg.type == 'note_off' else 0.1
            })
    
    # Noten in Balkendiagramm-Daten umwandeln
    y_labels = sorted(set(note['note'] for note in notes))
    y_map = {note: idx for idx, note in enumerate(y_labels)}
    
    fig, ax = plt.subplots(figsize=(8.3, 11.7))  # A4-Größe in Zoll: 8.3 x 11.7
    for note in notes:
        ax.broken_barh([(note['start'], note['duration'])], (y_map[note['note']], 1), facecolors='blue')
    
    # Achsen und Layout anpassen
    ax.set_yticks(range(len(y_labels)))
    ax.set_yticklabels([f"Note {note}" for note in y_labels])
    ax.set_xlabel('Time (seconds)')
    ax.set_ylabel('Notes')
    ax.set_title('MIDI Notes Visualized')
    ax.grid(True)
    
    # Diagramm speichern
    plt.tight_layout()
    plt.savefig(output_file)
    print(f"Diagram saved to {output_file}")

# Beispielaufruf
# Beispielaufruf
create_hallelujah_midi("hallelujah.mid")
midi_to_bars("hallelujah.mid")