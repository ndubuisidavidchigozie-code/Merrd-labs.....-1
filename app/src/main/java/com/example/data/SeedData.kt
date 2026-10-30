package com.example.data

import com.example.model.CbtQuestion
import com.example.model.GisFeature
import com.example.model.MistakeCategory
import com.example.model.MistakeItem
import com.example.model.NigerianState
import com.example.model.ResearchNote

object SeedData {

  val initialResearchNotes = listOf(
    ResearchNote(
      title = "RK4 Integrator Stability in Double Pendulum Chaos",
      content = "Runge-Kutta 4th Order numerical integration preserves symplectic phase-space volume significantly better than Euler-Cromer when tracking Lyapunov exponents in chaotic double planar pendulums. Observed bifurcation threshold at θ₁ = 124° with total mechanical energy variance under 0.003%.",
      author = "David",
      category = "ChaosTheory",
      tags = "#ChaosTheory #Physics #RK4 #Simulation",
      likes = 14
    ),
    ResearchNote(
      title = "Redox Stoichiometry in JAMB Permanganate Titrations",
      content = "In acidic media (dilute H₂SO₄), MnO₄⁻ is reduced to Mn²⁺ (5-electron transfer), whereas in neutral or alkaline media it yields MnO₂ (3 electrons). For UTME calculations: C₁V₁ / C₂V₂ = n₁ / n₂ where molar ratio of Fe²⁺ to MnO₄⁻ is strictly 5:1. Essential tip: Never acidify with HCl due to unwanted oxidation of Cl⁻ to toxic Cl₂ gas.",
      author = "Mercy",
      category = "Chemistry",
      tags = "#Chemistry #JAMB #Titration #Stoichiometry",
      likes = 18
    ),
    ResearchNote(
      title = "Equipotential Field Lines in Multipole Configurations",
      content = "Plotted 2D electrostatic equipotential surfaces across collinear quadrupoles. The gradient ∇V strictly aligns perpendicular to electric field vector field lines E = -∇V. Observed zero-field saddle point at (0, 0) with zero net Coulomb force.",
      author = "David",
      category = "Physics",
      tags = "#Physics #Electromagnetism #Coulomb #Vectors",
      likes = 11
    ),
    ResearchNote(
      title = "Cellular Respiration ATP Yield & Mitochondrial Chemiosmosis",
      content = "Proton motive force across inner mitochondrial cristae drives F₀F₁ ATP synthase rotation. Net eukaryotic glucose yield stabilizes at 30-32 ATP depending on malate-aspartate vs glycerol-3-phosphate shuttle efficiency in glycolysis.",
      author = "Mercy",
      category = "Biology",
      tags = "#Biology #Biochemistry #JAMB #CellBio",
      likes = 15
    ),
    ResearchNote(
      title = "Definite Integral Applications in Physics Work Calculations",
      content = "Evaluating W = ∫ F(x) dx for non-linear hookean springs with cubic stiffening terms F(x) = k₁x + k₂x³. Verified analytically via integration by parts and tested numerical trapezoidal approximation against our SC-991EX calculator engine.",
      author = "David",
      category = "Tech",
      tags = "#Tech #Calculus #Physics #SC991EX",
      likes = 9
    )
  )

  val cbtQuestions = listOf(
    CbtQuestion(
      id = 1,
      subject = "Physics",
      topic = "Mechanics",
      examYear = "JAMB 2024",
      questionText = "A projectile is launched from ground level with initial velocity u = 40 m/s at an angle θ = 30° to the horizontal. Neglecting air resistance and taking g = 10 m/s², what is the maximum height reached?",
      optionA = "15 m",
      optionB = "20 m",
      optionC = "30 m",
      optionD = "40 m",
      correctOption = 'B',
      explanation = "H_max = (u² sin²θ) / (2g). Here u = 40, sin(30°) = 0.5. u sinθ = 20 m/s. H_max = (20)² / (2 × 10) = 400 / 20 = 20 m.",
      formula = "H_{max} = \\frac{u^2 \\sin^2 \\theta}{2g}"
    ),
    CbtQuestion(
      id = 2,
      subject = "Physics",
      topic = "Waves & Optics",
      examYear = "JAMB 2023",
      questionText = "A radio transmitter broadcasts electromagnetic waves at a frequency of 100 MHz. Given that c = 3.0 × 10⁸ m/s, determine the wavelength of the transmission.",
      optionA = "0.33 m",
      optionB = "3.0 m",
      optionC = "30 m",
      optionD = "300 m",
      correctOption = 'B',
      explanation = "Using wave equation v = fλ. Here v = c = 3 × 10⁸ m/s, f = 100 MHz = 100 × 10⁶ Hz = 10⁸ Hz. λ = c / f = (3 × 10⁸) / (10⁸) = 3.0 m.",
      formula = "\\lambda = \\frac{c}{f}"
    ),
    CbtQuestion(
      id = 3,
      subject = "Chemistry",
      topic = "Stoichiometry & Titration",
      examYear = "JAMB 2024",
      questionText = "What volume of 0.10 mol/dm³ H₂SO₄ is required to neutralize 25.0 cm³ of 0.20 mol/dm³ NaOH completely?",
      optionA = "12.5 cm³",
      optionB = "25.0 cm³",
      optionC = "50.0 cm³",
      optionD = "6.25 cm³",
      correctOption = 'A',
      explanation = "Equation: H₂SO₄ + 2NaOH → Na₂SO₄ + 2H₂O. Ratio n_a/n_b = 1/2. Using (C_a × V_a) / (C_b × V_b) = 1/2: V_a = (C_b × V_b) / (2 × C_a) = (0.20 × 25.0) / (2 × 0.10) = 5.0 / 0.20 = 25.0 / 2 = 12.5 cm³.",
      formula = "\\frac{C_a V_a}{C_b V_b} = \\frac{n_a}{n_b}"
    ),
    CbtQuestion(
      id = 4,
      subject = "Chemistry",
      topic = "Atomic Structure",
      examYear = "JAMB 2022",
      questionText = "The electronic configuration of the iron(III) ion Fe³⁺ (atomic number Z = 26) in the ground state is:",
      optionA = "[Ar] 3d⁶ 4s²",
      optionB = "[Ar] 3d⁵ 4s⁰",
      optionC = "[Ar] 3d³ 4s²",
      optionD = "[Ar] 3d⁴ 4s¹",
      correctOption = 'B',
      explanation = "Neutral Fe (Z=26) has configuration [Ar] 3d⁶ 4s². Upon ionization to Fe³⁺, 2 electrons are removed first from the outermost 4s subshell, and 1 electron from the 3d subshell, yielding the stable half-filled d-subshell: [Ar] 3d⁵ 4s⁰.",
      formula = "\\text{Fe}^{3+}: [\\text{Ar}] 3d^5"
    ),
    CbtQuestion(
      id = 5,
      subject = "Mathematics",
      topic = "Calculus & Integration",
      examYear = "JAMB 2023",
      questionText = "Evaluate the definite integral ∫₀² (3x² - 2x + 1) dx.",
      optionA = "4",
      optionB = "6",
      optionC = "8",
      optionD = "10",
      correctOption = 'B',
      explanation = "Antiderivative F(x) = x³ - x² + x. Evaluated from 0 to 2: F(2) = (2)³ - (2)² + 2 = 8 - 4 + 2 = 6. F(0) = 0. Result = 6 - 0 = 6.",
      formula = "\\int_0^2 (3x^2 - 2x + 1) dx = [x^3 - x^2 + x]_0^2 = 6"
    ),
    CbtQuestion(
      id = 6,
      subject = "Mathematics",
      topic = "Matrices & Algebra",
      examYear = "JAMB 2021",
      questionText = "Find the determinant of the 2×2 matrix A = [[4, 2], [3, 5]].",
      optionA = "14",
      optionB = "26",
      optionC = "11",
      optionD = "-14",
      correctOption = 'A',
      explanation = "For 2×2 matrix [[a, b], [c, d]], det(A) = ad - bc = (4)(5) - (2)(3) = 20 - 6 = 14.",
      formula = "\\det(A) = ad - bc"
    ),
    CbtQuestion(
      id = 7,
      subject = "Biology",
      topic = "Genetics & Heredity",
      examYear = "JAMB 2024",
      questionText = "If a heterozygous man (Aa) for sickle cell trait marries a woman with sickle cell trait (Aa), what is the probability of having an offspring with sickle cell anaemia (aa)?",
      optionA = "0%",
      optionB = "25% (1/4)",
      optionC = "50% (1/2)",
      optionD = "75% (3/4)",
      correctOption = 'B',
      explanation = "Punnett square Aa × Aa yields AA (25%), Aa (50%), and aa (25%). The probability of aa is 1 in 4 (25%).",
      formula = "Aa \\times Aa \\rightarrow 1AA : 2Aa : 1aa"
    ),
    CbtQuestion(
      id = 8,
      subject = "Biology",
      topic = "Ecology & Ecosystems",
      examYear = "JAMB 2020",
      questionText = "In a typical ecological pyramid of energy, approximately what percentage of energy is transferred from one trophic level to the next higher level?",
      optionA = "1%",
      optionB = "10%",
      optionC = "50%",
      optionD = "90%",
      correctOption = 'B',
      explanation = "According to Lindeman's 10% law of trophic efficiency, only about 10% of the net energy produced at one trophic level is passed on to the next; the remainder is lost as respiratory heat or unconsumed biomass.",
      formula = "\\text{Trophic Efficiency} \\approx 10\\%"
    ),
    CbtQuestion(
      id = 9,
      subject = "Physics",
      topic = "Electric Fields & Capacitance",
      examYear = "JAMB 2022",
      questionText = "Two point charges of +4 μC and -4 μC are separated by a distance of 0.2 m in a vacuum. Taking 1/(4πε₀) = 9.0 × 10⁹ N·m²/C², what is the magnitude of the electrostatic force between them?",
      optionA = "3.6 N",
      optionB = "7.2 N",
      optionC = "1.8 N",
      optionD = "0.36 N",
      correctOption = 'A',
      explanation = "Coulomb's Law: F = k·|q₁q₂| / r² = (9 × 10⁹ × 4 × 10⁻⁶ × 4 × 10⁻⁶) / (0.2)² = (0.144) / 0.04 = 3.6 N (Attractive).",
      formula = "F = \\frac{k |q_1 q_2|}{r^2}"
    ),
    CbtQuestion(
      id = 10,
      subject = "Chemistry",
      topic = "Thermochemistry & Energetics",
      examYear = "JAMB 2023",
      questionText = "For an endothermic chemical reaction, which of the following statements regarding the enthalpy change (ΔH) and temperature change of the surrounding system is TRUE?",
      optionA = "ΔH > 0 and surrounding temperature decreases",
      optionB = "ΔH < 0 and surrounding temperature decreases",
      optionC = "ΔH > 0 and surrounding temperature increases",
      optionD = "ΔH < 0 and surrounding temperature increases",
      correctOption = 'A',
      explanation = "An endothermic reaction absorbs heat energy from the surrounding environment (ΔH is positive / ΔH > 0), resulting in a temperature drop in the immediate surroundings.",
      formula = "\\Delta H = H_{\\text{products}} - H_{\\text{reactants}} > 0"
    ),
    CbtQuestion(
      id = 11,
      subject = "Mathematics",
      topic = "Trigonometry & Identities",
      examYear = "JAMB 2021",
      questionText = "If sin θ = 3/5 where θ is an acute angle, find the exact value of cos(2θ).",
      optionA = "7/25",
      optionB = "24/25",
      optionC = "16/25",
      optionD = "-7/25",
      correctOption = 'A',
      explanation = "Double angle identity: cos(2θ) = 1 - 2 sin²θ = 1 - 2(3/5)² = 1 - 2(9/25) = 1 - 18/25 = 7/25.",
      formula = "\\cos(2\\theta) = 1 - 2\\sin^2\\theta"
    ),
    CbtQuestion(
      id = 12,
      subject = "Biology",
      topic = "Cell Biology & Physiology",
      examYear = "JAMB 2019",
      questionText = "Which cellular organelle is directly responsible for the synthesis of adenosine triphosphate (ATP) via oxidative phosphorylation in eukaryotic cells?",
      optionA = "Mitochondrion",
      optionB = "Ribosome",
      optionC = "Golgi Apparatus",
      optionD = "Lysosome",
      correctOption = 'A',
      explanation = "Mitochondria are the powerhouses of eukaryotic cells, utilizing the electron transport chain across inner cristae membranes to phosphorylate ADP into ATP via ATP synthase.",
      formula = "\\text{ADP} + \\text{P}_i \\rightarrow \\text{ATP}"
    ),
    CbtQuestion(
      id = 13,
      subject = "Physics",
      topic = "Thermal Physics",
      examYear = "JAMB 2024",
      questionText = "A metal rod of initial length 1.0 m expands by 0.002 m when heated through a temperature rise of 100 K. What is the linear expansivity (α) of the metal?",
      optionA = "2.0 × 10⁻⁵ K⁻¹",
      optionB = "2.0 × 10⁻⁴ K⁻¹",
      optionC = "5.0 × 10⁻⁴ K⁻¹",
      optionD = "1.0 × 10⁻⁵ K⁻¹",
      correctOption = 'A',
      explanation = "Linear expansivity formula: α = ΔL / (L₀ × Δθ) = 0.002 / (1.0 × 100) = 0.002 / 100 = 2.0 × 10⁻⁵ K⁻¹.",
      formula = "\\alpha = \\frac{\\Delta L}{L_0 \\Delta \\theta}"
    ),
    CbtQuestion(
      id = 14,
      subject = "Chemistry",
      topic = "Gas Laws & Kinetic Theory",
      examYear = "JAMB 2020",
      questionText = "A fixed mass of an ideal gas occupies 400 cm³ at 27 °C and 760 mmHg. What volume will it occupy at 127 °C under constant pressure?",
      optionA = "533.3 cm³",
      optionB = "300.0 cm³",
      optionC = "600.0 cm³",
      optionD = "450.0 cm³",
      correctOption = 'A',
      explanation = "Charles's Law: V₁/T₁ = V₂/T₂. Temperatures must be converted to Kelvin: T₁ = 27 + 273 = 300 K; T₂ = 127 + 273 = 400 K. V₂ = V₁ × (T₂ / T₁) = 400 × (400 / 300) = 1600 / 3 = 533.3 cm³.",
      formula = "\\frac{V_1}{T_1} = \\frac{V_2}{T_2}"
    )
  )

  val initialMistakes = listOf(
    MistakeItem(
      questionId = 3,
      subject = "Chemistry",
      topic = "Stoichiometry & Titration",
      questionText = "What volume of 0.10 mol/dm³ H₂SO₄ is required to neutralize 25.0 cm³ of 0.20 mol/dm³ NaOH completely?",
      optionA = "12.5 cm³",
      optionB = "25.0 cm³",
      optionC = "50.0 cm³",
      optionD = "6.25 cm³",
      correctOption = 'A',
      userSelectedOption = 'B',
      explanation = "Failed to account for the diprotic nature of H₂SO₄ (n_a = 1, n_b = 2 in C_aV_a/C_bV_b = 1/2).",
      category = MistakeCategory.CALCULATION_ERROR.name,
      consecutiveCorrect = 0,
      cleared = false,
      timeSpentSeconds = 135
    ),
    MistakeItem(
      questionId = 4,
      subject = "Chemistry",
      topic = "Atomic Structure",
      questionText = "The electronic configuration of the iron(III) ion Fe³⁺ (atomic number Z = 26) in the ground state is:",
      optionA = "[Ar] 3d⁶ 4s²",
      optionB = "[Ar] 3d⁵ 4s⁰",
      optionC = "[Ar] 3d³ 4s²",
      optionD = "[Ar] 3d⁴ 4s¹",
      correctOption = 'B',
      userSelectedOption = 'C',
      explanation = "Overlooked that 4s electrons are ionized prior to 3d electrons in transition elements.",
      category = MistakeCategory.CONCEPTUAL_GAP.name,
      consecutiveCorrect = 1, // Has 1 correct answer! 1 more needed to clear
      cleared = false,
      timeSpentSeconds = 85
    )
  )

  val formulaSheetData = listOf(
    Pair("Physics: Kinematics (SUVAT)", "v = u + at \ns = ut + ½at² \nv² = u² + 2as \ns = ½(u + v)t"),
    Pair("Physics: Newton & Energy", "F = ma \nW = F·d·cosθ \nE_k = ½mv² \nE_p = mgh"),
    Pair("Physics: Optics & Waves", "v = fλ \nT = 1/f \n1/f = 1/u + 1/v \nn = c/v = sin i / sin r"),
    Pair("Physics: Electrostatics", "F = k·|q₁q₂| / r² \nE = F/q = kQ/r² \nV = kQ/r \nC = Q/V = εA/d"),
    Pair("Chemistry: Gas Laws", "PV = nRT (R=8.314 J/mol·K) \nP₁V₁/T₁ = P₂V₂/T₂ \nP_total = P_A + P_B"),
    Pair("Chemistry: Solutions & pH", "C = n/V = m/(M·V) \npH = -log₁₀[H⁺] \npOH = -log₁₀[OH⁻] \npH + pOH = 14"),
    Pair("Chemistry: Thermochemistry", "ΔH = H_products - H_reactants \nq = mcΔT \nk = A·e^(-E_a/RT)"),
    Pair("Mathematics: Quadratic & Calculus", "x = (-b ± √(b² - 4ac)) / (2a) \nd/dx(x^n) = n·x^(n-1) \n∫ x^n dx = x^(n+1)/(n+1) + C"),
    Pair("Mathematics: Trig Identities", "sin²θ + cos²θ = 1 \nsin(2θ) = 2sinθ·cosθ \ncos(2θ) = cos²θ - sin²θ"),
    Pair("Biology: Genetics & Population", "Hardy-Weinberg: p² + 2pq + q² = 1 \np + q = 1 \nMagnification = Image / Actual")
  )

  val nigerianStates = listOf(
    NigerianState("Abia", "Umuahia", 17, "Aba, Ohafia", "Freshwater Swamp & Rainforest", 0.58f, 0.77f),
    NigerianState("Adamawa", "Yola", 21, "Mubi, Numan", "Guinea Savanna", 0.82f, 0.44f),
    NigerianState("Akwa Ibom", "Uyo", 31, "Eket, Ikot Ekpene", "Mangrove & Rainforest", 0.61f, 0.83f),
    NigerianState("Anambra", "Awka", 21, "Onitsha, Nnewi", "Tropical Rainforest", 0.53f, 0.73f),
    NigerianState("Bauchi", "Bauchi", 20, "Azare, Misau", "Sudan Savanna", 0.69f, 0.35f),
    NigerianState("Bayelsa", "Yenagoa", 8, "Brass, Ogbia", "Mangrove Swamp", 0.47f, 0.84f),
    NigerianState("Benue", "Makurdi", 23, "Gboko, Otukpo", "Guinea Savanna", 0.64f, 0.60f),
    NigerianState("Borno", "Maiduguri", 27, "Biu, Bama", "Sahel Savanna", 0.88f, 0.25f),
    NigerianState("Cross River", "Calabar", 18, "Ikom, Ogoja", "Tropical Rainforest", 0.68f, 0.76f),
    NigerianState("Delta", "Asaba", 25, "Warri, Sapele", "Mangrove & Freshwater", 0.46f, 0.75f),
    NigerianState("Ebonyi", "Abakaliki", 13, "Afikpo, Onueke", "Guinea Savanna", 0.61f, 0.71f),
    NigerianState("Edo", "Benin City", 18, "Auchi, Ekpoma", "Tropical Rainforest", 0.43f, 0.68f),
    NigerianState("Ekiti", "Ado-Ekiti", 16, "Ikole, Ijero", "Tropical Rainforest", 0.37f, 0.62f),
    NigerianState("Enugu", "Enugu", 17, "Nsukka, Oji River", "Guinea Savanna", 0.56f, 0.70f),
    NigerianState("FCT (Abuja)", "Abuja", 6, "Gwagwalada, Bwari", "Guinea Savanna", 0.52f, 0.50f),
    NigerianState("Gombe", "Gombe", 11, "Kaltungo, Bajoga", "Sudan Savanna", 0.76f, 0.36f),
    NigerianState("Imo", "Owerri", 27, "Orlu, Okigwe", "Tropical Rainforest", 0.54f, 0.78f),
    NigerianState("Jigawa", "Dutse", 27, "Hadejia, Gumel", "Sudan Savanna", 0.65f, 0.21f),
    NigerianState("Kaduna", "Kaduna", 23, "Zaria, Kafanchan", "Guinea Savanna", 0.53f, 0.35f),
    NigerianState("Kano", "Kano", 44, "Wudil, Rano", "Sudan Savanna", 0.58f, 0.23f),
    NigerianState("Katsina", "Katsina", 34, "Daura, Funtua", "Sahel & Sudan Savanna", 0.50f, 0.17f),
    NigerianState("Kebbi", "Birnin Kebbi", 21, "Argungu, Yauri", "Sudan Savanna", 0.31f, 0.26f),
    NigerianState("Kogi", "Lokoja", 21, "Okene, Idah", "Guinea Savanna", 0.48f, 0.58f),
    NigerianState("Kwara", "Ilorin", 16, "Offa, Omu-Aran", "Guinea Savanna", 0.33f, 0.52f),
    NigerianState("Lagos", "Ikeja", 20, "Lagos Island, Badagry", "Mangrove Swamp", 0.26f, 0.73f),
    NigerianState("Nasarawa", "Lafia", 13, "Keffi, Akwanga", "Guinea Savanna", 0.58f, 0.52f),
    NigerianState("Niger", "Minna", 25, "Bida, Suleja, Kontagora", "Guinea Savanna", 0.40f, 0.42f),
    NigerianState("Ogun", "Abeokuta", 20, "Ijebu-Ode, Sagamu", "Tropical Rainforest", 0.28f, 0.68f),
    NigerianState("Ondo", "Akure", 18, "Ondo Town, Owo", "Tropical Rainforest", 0.36f, 0.67f),
    NigerianState("Osun", "Osogbo", 30, "Ile-Ife, Ilesa", "Tropical Rainforest", 0.32f, 0.62f),
    NigerianState("Oyo", "Ibadan", 33, "Ogbomoso, Oyo", "Guinea Savanna & Forest", 0.25f, 0.59f),
    NigerianState("Plateau", "Jos", 17, "Bukuru, Pankshin", "Guinea Savanna (Montane)", 0.62f, 0.46f),
    NigerianState("Rivers", "Port Harcourt", 23, "Bonny, Okrika", "Mangrove Swamp", 0.52f, 0.82f),
    NigerianState("Sokoto", "Sokoto", 23, "Tambuwal, Wurno", "Sahel Savanna", 0.35f, 0.15f),
    NigerianState("Taraba", "Jalingo", 16, "Wukari, Bali", "Guinea Savanna", 0.73f, 0.55f),
    NigerianState("Yobe", "Damaturu", 17, "Potiskum, Gashua", "Sahel Savanna", 0.79f, 0.22f),
    NigerianState("Zamfara", "Gusau", 14, "Talata Mafara, Kaura", "Sudan Savanna", 0.43f, 0.24f)
  )

  val gisFeatures = listOf(
    // Rivers & Drainage
    GisFeature(
      id = "river_niger",
      name = "River Niger",
      layer = "RIVERS",
      formationType = "Exotic Insequent River System (Length 4,180 km)",
      economicSignificance = "Hydroelectric power generation at Kainji and Jebba dams; inland water transportation; irrigation for rice cultivation.",
      pastJambQuestions = listOf(
        "Which major river system enters Nigeria through Kebbi State and forms a confluence at Lokoja? [Ans: River Niger]",
        "Kainji Dam is situated on which Nigerian river? [Ans: River Niger]",
        "The crescent/boomerang course of River Niger across West Africa is primarily attributed to? [Ans: River capture of two former distinct river systems]"
      ),
      posX = 0.38f,
      posY = 0.42f
    ),
    GisFeature(
      id = "river_benue",
      name = "River Benue & Confluence",
      layer = "RIVERS",
      formationType = "Major Eastern Tributary & Y-Shaped Confluence at Lokoja",
      economicSignificance = "Extensive flood plains for yam and beniseed cultivation; year-round navigation hub between Middle Belt and Atlantic coast.",
      pastJambQuestions = listOf(
        "At which historic town do River Niger and River Benue converge? [Ans: Lokoja, Kogi State]",
        "River Benue originates from which highland system? [Ans: Adamawa Highlands in Cameroon]",
        "The confluence of Rivers Niger and Benue features what type of drainage pattern? [Ans: Dendritic drainage system]"
      ),
      posX = 0.52f,
      posY = 0.58f
    ),
    GisFeature(
      id = "niger_delta",
      name = "Niger Delta Distributaries",
      layer = "RIVERS",
      formationType = "Arcuate River Delta & Mangrove Estuary Ecosystem",
      economicSignificance = "Epicenter of Nigeria's petroleum crude oil extraction and LNG terminals; intensive artisanal fishing.",
      pastJambQuestions = listOf(
        "The Niger Delta is classified as what kind of delta? [Ans: Arcuate Delta]",
        "Which dominant tree species anchors the coastal tidal swamps of the Niger Delta? [Ans: Red Mangrove (Rhizophora mangle)]",
        "The distributaries of the Niger Delta empty into which oceanic basin? [Ans: Gulf of Guinea / Bight of Bonny]"
      ),
      posX = 0.49f,
      posY = 0.81f
    ),

    // Relief Features
    GisFeature(
      id = "jos_plateau",
      name = "Jos Plateau (Relief)",
      layer = "RELIEF",
      formationType = "Volcanic & Granitic Residual Horst / Highland (Avg 1,280 m)",
      economicSignificance = "Temperate microclimate for Irish potato and temperate vegetable farming; historic open-cast tin & columbite mining.",
      pastJambQuestions = listOf(
        "The cool temperate-like climate of Jos is primarily a result of? [Ans: High altitude / elevation above sea level]",
        "Which mineral ore has been historically mined in commercial quantities on the Jos Plateau? [Ans: Tin / Cassiterite and Columbite]",
        "The Jos Plateau serves as the radial hydrographic drainage center for which rivers? [Ans: Kaduna, Gongola, and Hadejia rivers]"
      ),
      posX = 0.63f,
      posY = 0.45f
    ),
    GisFeature(
      id = "obudu_plateau",
      name = "Obudu Plateau & Highlands",
      layer = "RELIEF",
      formationType = "Precambrian Basement Complex Horst / Ridge (1,580 m)",
      economicSignificance = "Major eco-tourism and mountain resort center; dairy farming on highland pastures.",
      pastJambQuestions = listOf(
        "Obudu Plateau is situated in which geopolitical zone of Nigeria? [Ans: South-South / Cross River State]",
        "What form of precipitation is commonly observed on the high peaks of Obudu? [Ans: Orographic / Relief rainfall]",
        "The soils of the Obudu Plateau are derived predominantly from what rock formation? [Ans: Basement Complex granites and gneisses]"
      ),
      posX = 0.70f,
      posY = 0.74f
    ),
    GisFeature(
      id = "adamawa_mandara",
      name = "Adamawa Highlands & Mandara Mountains",
      layer = "RELIEF",
      formationType = "Volcanic & Fold Mountain Ridge (Max elevation 2,419 m at Chappal Waddi)",
      economicSignificance = "Highest peak in Nigeria (Chappal Waddi / Gangirwal); watershed separating Benue and Chad basins; cattle transhumance.",
      pastJambQuestions = listOf(
        "What is the highest mountain peak in Nigeria? [Ans: Chappal Waddi / 2,419 meters in Taraba/Adamawa]",
        "The Mandara mountain chain forms a natural geopolitical border between Nigeria and? [Ans: Cameroon Republic]",
        "Terrace farming is extensively practiced on which Nigerian highland due to steep slopes? [Ans: Mandara Mountains]"
      ),
      posX = 0.84f,
      posY = 0.43f
    ),

    // Minerals & Resources
    GisFeature(
      id = "min_oil_gas",
      name = "Crude Oil & Natural Gas (Niger Delta)",
      layer = "MINERALS",
      formationType = "Sedimentary Tertiary Agbada Formation / Petroleum Traps",
      economicSignificance = "Provides >85% of Nigeria's foreign exchange earnings and government fiscal revenues; natural gas power feedstock.",
      pastJambQuestions = listOf(
        "In which sedimentary basin are Nigeria's primary commercial petroleum reserves located? [Ans: Niger Delta Sedimentary Basin]",
        "At which location in Bayelsa State was crude oil first struck in commercial quantities in 1956? [Ans: Oloibiri]",
        "Which geological rock type serves as the primary reservoir rock for oil in Nigeria? [Ans: Sandstone]"
      ),
      posX = 0.48f,
      posY = 0.82f
    ),
    GisFeature(
      id = "min_tin_columbite",
      name = "Tin & Columbite Belt (Jos)",
      layer = "MINERALS",
      formationType = "Pneumatolytic Younger Granite Pegmatites",
      economicSignificance = "Used in heat-resistant aerospace alloys, capacitors, and metallurgy.",
      pastJambQuestions = listOf(
        "Columbite is commonly found in association with which metal ore in Plateau State? [Ans: Cassiterite / Tin Ore]",
        "In which rock unit does tin mineralization occur on the Jos Plateau? [Ans: Younger Granites of Jurassic age]",
        "What environmental degradation is predominantly caused by open-cast tin mining in Jos? [Ans: Gully erosion and deep mine-tailing ponds]"
      ),
      posX = 0.62f,
      posY = 0.46f
    ),
    GisFeature(
      id = "min_coal_enugu",
      name = "Sub-Bituminous Coal (Enugu Basin)",
      layer = "MINERALS",
      formationType = "Cretaceous Mamu Formation (Coal Measures)",
      economicSignificance = "Historic fuel for Nigerian railways and thermal power generation at Oji River; smokeless fuel potential.",
      pastJambQuestions = listOf(
        "Which Nigerian city is historically renowned as the 'Coal City'? [Ans: Enugu]",
        "The type of coal found in commercial seams across Enugu is? [Ans: Sub-bituminous coal]",
        "In which geological epoch was the Nigerian coal sequence deposited? [Ans: Upper Cretaceous (Campanian-Maastrichtian)]"
      ),
      posX = 0.57f,
      posY = 0.69f
    ),
    GisFeature(
      id = "min_limestone",
      name = "Limestone Belts (Ewekoro & Calabar)",
      layer = "MINERALS",
      formationType = "Tertiary & Cretaceous Marine Carbonate Formations",
      economicSignificance = "Essential raw material for cement factories (Dangote, Lafarge, BUA); agricultural soil conditioning.",
      pastJambQuestions = listOf(
        "Limestone deposits at Ewekoro and Sagamu in Ogun State support what major manufacturing industry? [Ans: Cement industry]",
        "Chemical weathering of limestone rocks results in distinctive surface landforms known as? [Ans: Karst topography]",
        "Limestone is composed predominantly of which mineral compound? [Ans: Calcium Carbonate / CaCO₃]"
      ),
      posX = 0.30f,
      posY = 0.68f
    ),
    GisFeature(
      id = "min_iron_itakpe",
      name = "Iron Ore (Itakpe & Agbaja)",
      layer = "MINERALS",
      formationType = "Banded Iron Formation (BIF) & Oolitic Ironstone",
      economicSignificance = "Primary iron ore feed for the Ajaokuta Steel Complex and Delta Steel Company, Aladja.",
      pastJambQuestions = listOf(
        "The Itakpe iron ore deposit in Kogi State was developed to supply feed to which industrial plant? [Ans: Ajaokuta Steel Plant]",
        "The primary mineral form of iron extracted at Itakpe is? [Ans: Hematite and Magnetite]",
        "Agbaja plateau ironstone is characterized by what texture? [Ans: Oolitic / Pisolitic texture]"
      ),
      posX = 0.47f,
      posY = 0.57f
    ),
    GisFeature(
      id = "min_gold",
      name = "Gold Deposits (Zamfara & Osun)",
      layer = "MINERALS",
      formationType = "Schist Belts & Hydrothermal Quartz Veins",
      economicSignificance = "Precious metal reserves supporting regulated mining, jewelry manufacture, and national gold reserves.",
      pastJambQuestions = listOf(
        "In which rock formation does lode gold occur in southwestern Nigeria (e.g., Iperindo, Osun State)? [Ans: Precambrian Schist Belts]",
        "Gold panning along river banks exploits what type of deposit? [Ans: Alluvial / Placer gold deposits]",
        "What toxic chemical hazard has historically affected artisanal gold mining communities in Zamfara? [Ans: Lead poisoning during ore crushing]"
      ),
      posX = 0.40f,
      posY = 0.28f
    ),

    // Africa Regional Geography Features
    GisFeature(
      id = "africa_rift_valley",
      name = "East African Rift Valley",
      layer = "AFRICA",
      formationType = "Continental Divergent Tectonic Graben (6,000 km)",
      economicSignificance = "Hosts great African lakes, geothermal energy wells in Kenya (Olkaria), and unique biodiversity.",
      pastJambQuestions = listOf(
        "The East African Rift Valley was formed by which tectonic process? [Ans: Tensional stress causing normal faulting and graben subsidence]",
        "Which of the following lakes lies within the Western branch of the East African Rift Valley? [Ans: Lake Tanganyika]",
        "Which active volcanic mountain is associated with the East African Rift System? [Ans: Mount Nyiragongo / Ol Doinyo Lengai]"
      ),
      posX = 0.72f,
      posY = 0.56f
    ),
    GisFeature(
      id = "africa_mount_kilimanjaro",
      name = "Mount Kilimanjaro (5,895 m)",
      layer = "AFRICA",
      formationType = "Stratovolcano / Highest Peak in Africa",
      economicSignificance = "Eco-tourism flagship; ecological zonation from tropical forest to equatorial snow and glacier fields.",
      pastJambQuestions = listOf(
        "What is the highest mountain peak on the African continent? [Ans: Mount Kilimanjaro in Tanzania (5,895 m)]",
        "Why does Mount Kilimanjaro retain permanent snow despite being located near the equator? [Ans: High altitude causes atmospheric lapse-rate cooling below freezing]",
        "Mount Kilimanjaro is classified as what type of volcano? [Ans: Dormant stratovolcano / composite cone]"
      ),
      posX = 0.74f,
      posY = 0.62f
    ),
    GisFeature(
      id = "africa_nile_river",
      name = "River Nile & Aswan Dam",
      layer = "AFRICA",
      formationType = "Longest River in the World (6,650 km)",
      economicSignificance = "Irrigation for Egyptian cotton and agriculture; Lake Nasser reservoir and Aswan High Dam hydroelectricity.",
      pastJambQuestions = listOf(
        "The White Nile and Blue Nile converge at which African capital? [Ans: Khartoum, Sudan]",
        "The Aswan High Dam controls flooding and provides irrigation along which river? [Ans: River Nile]",
        "What is the source reservoir for the Blue Nile? [Ans: Lake Tana in the Ethiopian Highlands]"
      ),
      posX = 0.68f,
      posY = 0.22f
    ),
    GisFeature(
      id = "africa_lake_chad",
      name = "Lake Chad (Shrinking Boundary)",
      layer = "AFRICA",
      formationType = "Endorheic Drainage Basin / Mega-Chad Paleolake Remnant",
      economicSignificance = "Livelihood support for 30 million people across Nigeria, Chad, Niger, and Cameroon; severely shrunken by >90% since 1963.",
      pastJambQuestions = listOf(
        "Lake Chad is characterized by having no surface oceanic outlet; such a basin is termed? [Ans: Endorheic / Inland drainage basin]",
        "Which major river contributes over 80% of annual water inflow into Lake Chad? [Ans: Chari-Logone River System]",
        "The dramatic surface shrinkage of Lake Chad since the 1960s is primarily attributed to? [Ans: Climate change, prolonged Sahelian droughts, and intensive irrigation diversions]"
      ),
      posX = 0.62f,
      posY = 0.38f
    )
  )
}
