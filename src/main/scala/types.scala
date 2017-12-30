package object types {
  type NoteNumber = Int

  type PitchClass = Int
  type Step = Int
  type Accidental = Int

  type Simultaneous[A] = Set[A]
  type OneOf[A] = Set[A]
}