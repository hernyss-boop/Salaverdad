package com.example.data

import com.example.model.Enigma
import com.example.model.EnigmaType
import com.example.model.Hint
import com.example.model.InvestigationNote
import com.example.model.TeacherGuideItem

object EscapeRoomRepository {

    const val TEACHER_SECRET_KEY = "1980"
    const val DEFAULT_ROOM_PASSWORD = "VERITAS"
    const val FINAL_VAULT_CODE = "7392"
    const val TOTAL_TIME_SECONDS = 30 * 60 // 30 minutes

    val enigmas: List<Enigma> = listOf(
        Enigma(
            id = 1,
            title = "Enigma 1: La Trampa del Clickbait",
            subtitle = "Módulo de Filtrado de Titulares",
            topic = "Titulares sensacionalistas vs información rigurosa",
            storyIntro = "¡ALERTA EN LA TERMINAL! La red desinformativa DeepEcho ha inundado el portal escolar con titulares alarmistas que buscan sembrar el pánico antes de la votación estudiantil. Debes aislar los dos titulares fraudulentos e identificar la emoción primaria que intentan manipular.",
            missionInstruction = "Selecciona los 2 titulares que clasifican como CLICKBAIT ENGAÑOSO y luego indica qué emoción buscan provocar para anular el pensamiento crítico.",
            type = EnigmaType.CLICKBAIT_ANALYSIS,
            givesDigit = true,
            digitValue = "7",
            digitClueName = "Dígito Alfa (Titular)",
            hints = listOf(
                Hint(
                    level = 1,
                    title = "Pista 1 (Gratis)",
                    text = "El clickbait suele usar mayúsculas innecesarias, signos de exclamación exagerados ('¡NO LO CREERÁS!') y apela a emociones viscerales como el miedo o la urgencia sin citar ningún dato concreto.",
                    costDescription = "Sin costo en puntos",
                    penalty = 0.0f
                ),
                Hint(
                    level = 2,
                    title = "Pista 2 (-0.25 pts)",
                    text = "Fíjate en los titulares que prometen catástrofes inminentes ('CERRARÁN EL COLEGIO') o secretos oscuros sin fuentes oficiales. La emoción que explotan es el MIEDO/URGENCIA.",
                    costDescription = "-0.25 puntos",
                    penalty = 0.25f
                ),
                Hint(
                    level = 3,
                    title = "Pista 3 (Revelación)",
                    text = "RESPUESTA: Titulares fraudulentos: 1 y 4 ('¡URGENTE: Suspenderán las vacaciones!' y '¡El oscuro plan que nadie quiere que sepas!'). Emoción manipulada: MIEDO.",
                    costDescription = "0 pts en este enigma",
                    penalty = 1.5f
                )
            ),
            pedagogicalSummary = "El clickbait distorsiona la realidad para generar clics o pánico. Los medios confiables titulan de forma descriptiva, mesurada y atribuyendo las fuentes."
        ),
        Enigma(
            id = 2,
            title = "Enigma 2: El Laberinto de las URLs Clonadas",
            subtitle = "Inspección de Spoofing y Certificados",
            topic = "Detección de sitios web falsos y dominios engañosos",
            storyIntro = "DeepEcho ha enviado un comunicado a los delegados de curso aparentando ser un sitio oficial del Ministerio de Educación. El servidor ha interceptado 4 enlaces. Debes identificar cuál de ellos es la URL FALSA (Spoofing) y escribir la palabra clave que valida la seguridad.",
            missionInstruction = "Elige la URL falsa que intenta engañar con typo-squatting y escribe abajo la palabra de seguridad que garantiza una conexión legítima: 'CERTIFICADO'.",
            type = EnigmaType.URL_INSPECTION,
            givesDigit = false,
            digitValue = null,
            digitClueName = null,
            hints = listOf(
                Hint(
                    level = 1,
                    title = "Pista 1 (Gratis)",
                    text = "Observa con lupa las extensiones de dominio (.com vs .xyz) y los guiones extras. Un sitio gubernamental oficial de Argentina termina en '.gob.ar' o '.edu.ar'.",
                    costDescription = "Sin costo en puntos",
                    penalty = 0.0f
                ),
                Hint(
                    level = 2,
                    title = "Pista 2 (-0.25 pts)",
                    text = "La URL clonada utiliza HTTP en vez de HTTPS y agrega '.xyz' al final del dominio. La palabra que debes escribir es 'CERTIFICADO'.",
                    costDescription = "-0.25 puntos",
                    penalty = 0.25f
                ),
                Hint(
                    level = 3,
                    title = "Pista 3 (Revelación)",
                    text = "RESPUESTA: La URL falsa es 'http://www.min-educacion.gob.ar.portal-alumnos.xyz/comunicado'. La palabra clave a escribir es CERTIFICADO.",
                    costDescription = "0 pts en este enigma",
                    penalty = 1.5f
                )
            ),
            pedagogicalSummary = "El 'Spoofing' de dominios imita nombres de marcas conocidas para robar datos o difundir mentiras. Verificar la barra de direcciones y el candado SSL es clave."
        ),
        Enigma(
            id = 3,
            title = "Enigma 3: La Trampa Visual Descontextualizada",
            subtitle = "Protocolo de Búsqueda Inversa de Imágenes",
            topic = "Uso de fotos antiguas o fuera de contexto para engañar",
            storyIntro = "Circula una imagen dramática que muestra supuestos daños estructurales en el gimnasio del colegio ocurridos esta mañana. Sospechamos que es una foto reciclada de otro país. Para desactivar la difusión, debes ordenar correctamente los 4 pasos del protocolo de Búsqueda Inversa.",
            missionInstruction = "Ordena secuencialmente los 4 pasos del método de verificación de imágenes desde el 1° hasta el 4°.",
            type = EnigmaType.IMAGE_ORDERING,
            givesDigit = true,
            digitValue = "3",
            digitClueName = "Dígito Beta (Imagen)",
            hints = listOf(
                Hint(
                    level = 1,
                    title = "Pista 1 (Gratis)",
                    text = "Primero debes aislar el archivo, luego usar una herramienta de rastreo visual (Google Lens o TinEye), después analizar las fechas de los primeros resultados y finalmente comparar con fuentes oficiales.",
                    costDescription = "Sin costo en puntos",
                    penalty = 0.0f
                ),
                Hint(
                    level = 2,
                    title = "Pista 2 (-0.25 pts)",
                    text = "El orden lógico es: 1. Guardar/Copiar enlace de imagen -> 2. Cargar en Google Lens o TinEye -> 3. Filtrar por publicación más antigua -> 4. Confirmar contexto real.",
                    costDescription = "-0.25 puntos",
                    penalty = 0.25f
                ),
                Hint(
                    level = 3,
                    title = "Pista 3 (Revelación)",
                    text = "RESPUESTA: Orden exacto: [1, 2, 3, 4]. 1° Guardar imagen, 2° Subir a Google Lens, 3° Buscar fecha más antigua, 4° Contrastar ubicación real.",
                    costDescription = "0 pts en este enigma",
                    penalty = 1.5f
                )
            ),
            pedagogicalSummary = "La gran mayoría de imágenes de noticias falsas no son creadas con IA sino fotos reales reutilizadas fuera de su fecha y lugar original. La búsqueda inversa las desmonta en segundos."
        ),
        Enigma(
            id = 4,
            title = "Enigma 4: La Red de Bots y Cuentas Clon",
            subtitle = "Análisis Forense de Redes Sociales",
            topic = "Identificación de bots, cuentas automatizadas y astroturfing",
            storyIntro = "El hashtag #ParoEstudiantilTotal se volvió tendencia escolar en 15 minutos impulsado por cuentas dudosas. Analiza el perfil interceptado '@arg_estudiante_94821' y marca las 3 señales reveladoras de que se trata de un BOT automatizado.",
            missionInstruction = "Marca exactamente las 3 casillas que delatan a un perfil bot automatizado creado para desinformar.",
            type = EnigmaType.BOT_DETECTION,
            givesDigit = true,
            digitValue = "9",
            digitClueName = "Dígito Gamma (Bots)",
            hints = listOf(
                Hint(
                    level = 1,
                    title = "Pista 1 (Gratis)",
                    text = "Los bots suelen tener nombres con números aleatorios, cuentas creadas hace días, miles de publicaciones repetidas sin interacción genuina y fotos de personas generadas artificialmente.",
                    costDescription = "Sin costo en puntos",
                    penalty = 0.0f
                ),
                Hint(
                    level = 2,
                    title = "Pista 2 (-0.25 pts)",
                    text = "Descarta las opciones normales como tener amigos del colegio. Enfócate en: 1) Fecha de creación reciente con miles de retweets, 2) Usuario con números al azar y foto de stock/IA, 3) Publicaciones idénticas cada 10 segundos.",
                    costDescription = "-0.25 puntos",
                    penalty = 0.25f
                ),
                Hint(
                    level = 3,
                    title = "Pista 3 (Revelación)",
                    text = "RESPUESTA: Marca las opciones: A (Cuenta creada hace 3 días con 2000 tuits), B (Nombre @user_94821 con foto generada por IA) y C (Publica el mismo mensaje copiado cada 15 segundos).",
                    costDescription = "0 pts en este enigma",
                    penalty = 1.5f
                )
            ),
            pedagogicalSummary = "El fenómeno de 'Astroturfing' simula un apoyo popular espontáneo mediante granjas de bots programadas para manipular las tendencias de los algoritmos."
        ),
        Enigma(
            id = 5,
            title = "Enigma 5: El Test de Calidad Informativa CRAAP",
            subtitle = "Sincronización de Criterios de Evaluación",
            topic = "Criterios para evaluar la credibilidad de cualquier fuente",
            storyIntro = "Para restaurar el cortafuegos de la biblioteca escolar, debes vincular cada criterio del método internacional CRAAP con la pregunta de control correcta.",
            missionInstruction = "Empareja cada criterio de verificación con la pregunta clave que debe hacerse un investigador para no caer en engaños.",
            type = EnigmaType.CRAAP_PAIRING,
            givesDigit = false,
            digitValue = null,
            digitClueName = null,
            hints = listOf(
                Hint(
                    level = 1,
                    title = "Pista 1 (Gratis)",
                    text = "CRAAP evalúa: Actualidad (tiempo), Relevancia (importancia), Autoridad (quién lo dice), Exactitud (veracidad de datos) y Propósito (intención u objetivo).",
                    costDescription = "Sin costo en puntos",
                    penalty = 0.0f
                ),
                Hint(
                    level = 2,
                    title = "Pista 2 (-0.25 pts)",
                    text = "Autoridad se pregunta por las credenciales del autor. Actualidad por la fecha. Exactitud por las evidencias y fuentes. Propósito por la intención oculta.",
                    costDescription = "-0.25 puntos",
                    penalty = 0.25f
                ),
                Hint(
                    level = 3,
                    title = "Pista 3 (Revelación)",
                    text = "RESPUESTA: 1->Fecha de publicación reciente, 2->Credenciales y trayectoria del autor, 3->Datos comprobables con enlaces a estudios, 4->Intención de informar vs manipular o vender.",
                    costDescription = "0 pts en este enigma",
                    penalty = 1.5f
                )
            ),
            pedagogicalSummary = "El test CRAAP es un estándar académico universal para verificar si un contenido tiene rigor científico o es mero rumor sin respaldo."
        ),
        Enigma(
            id = 6,
            title = "Enigma 6: El Protocolo de Verificación Lateral (SIFT)",
            subtitle = "Hackeo del Núcleo Central de DeepEcho",
            topic = "Lectura lateral y chequeo cruzado de información",
            storyIntro = "¡Estamos a las puertas del servidor principal! Los periodistas profesionales de verificación no leen de arriba a abajo una noticia dudosa; abren pestañas a los lados para investigar qué dicen otros medios. Este es el método SIFT de Mike Caulfield. Completa los 4 pilares.",
            missionInstruction = "Completa las 4 palabras faltantes del protocolo de verificación lateral seleccionando la opción correcta en cada espacio.",
            type = EnigmaType.SIFT_CLOZE,
            givesDigit = true,
            digitValue = "2",
            digitClueName = "Dígito Delta (Verificación)",
            hints = listOf(
                Hint(
                    level = 1,
                    title = "Pista 1 (Gratis)",
                    text = "SIFT son 4 siglas en inglés traducidas: Stop (Detenerse), Investigate (Investigar), Find (Buscar) y Trace (Rastrear).",
                    costDescription = "Sin costo en puntos",
                    penalty = 0.0f
                ),
                Hint(
                    level = 2,
                    title = "Pista 2 (-0.25 pts)",
                    text = "Espacio 1: Detenerse. Espacio 2: Fuente. Espacio 3: Mejor. Espacio 4: Original.",
                    costDescription = "-0.25 puntos",
                    penalty = 0.25f
                ),
                Hint(
                    level = 3,
                    title = "Pista 3 (Revelación)",
                    text = "RESPUESTA: 1° DETENERSE, 2° FUENTE, 3° MEJOR, 4° ORIGINAL.",
                    costDescription = "0 pts en este enigma",
                    penalty = 1.5f
                )
            ),
            pedagogicalSummary = "La lectura lateral consiste en salir de la página sospechosa para ver qué dicen organizaciones independientes de chequeo de datos sobre ese autor o afirmación."
        )
    )

    val investigationNotes: List<InvestigationNote> = listOf(
        InvestigationNote(
            id = "note_1",
            title = "Anatomía del Clickbait",
            category = "Titulares & Emociones",
            summary = "Estrategias de redacción pensadas para capturar atención mediante la exageración.",
            keyPoints = listOf(
                "Uso de mayúsculas sostenidas y signos de admiración múltiples.",
                "Ocultamiento de información clave para obligar a entrar ('No creerás lo que pasó...').",
                "Apelación directa a emociones básicas: indignación, miedo, sorpresa extrema o codicia.",
                "Desconexión entre el titular impactante y el cuerpo real de la noticia."
            ),
            practicalTip = "Si un titular te provoca una furia o alarma inmediata, no lo compartas. Respira y busca el dato en un diario serio."
        ),
        InvestigationNote(
            id = "note_2",
            title = "Detección de URLs Falsas (Spoofing)",
            category = "Seguridad Web",
            summary = "Los sitios falsos imitan dominios respetables agregando pequeñas variaciones.",
            keyPoints = listOf(
                "Typosquatting: cambiar una letra (ej. faceb00k.com, lanaci0n.com).",
                "Subdominios tramposos: 'clarin.noticias-argentina.xyz' (el dominio real es noticias-argentina.xyz, no Clarín).",
                "Extensiones poco comunes (.top, .xyz, .cc, .buzz) asociadas a servidores temporales.",
                "Ausencia de protocolo seguro HTTPS y certificado digital válido."
            ),
            practicalTip = "Mira siempre la palabra inmediatamente anterior al último punto y la extensión: ese es el verdadero dueño del dominio."
        ),
        InvestigationNote(
            id = "note_3",
            title = "Búsqueda Inversa de Imágenes",
            category = "Verificación Visual",
            summary = "Cómo descubrir si una foto es reciclada, descontextualizada o trucada.",
            keyPoints = listOf(
                "Herramientas gratuitas: Google Lens, TinEye, RevEye y Bing Visual Search.",
                "Identificar la fecha de la primera indexación en internet.",
                "Fijarse en detalles incongruentes: letreros en otro idioma, clima diferente, marcas de coches no locales.",
                "Verificar si las caras presentan anomalías propias de IA (pupilas deformes, manos con 6 dedos)."
            ),
            practicalTip = "Haz clic derecho en la imagen y elige 'Buscar imagen con Google'. Te dirá en segundos dónde y cuándo apareció por primera vez."
        ),
        InvestigationNote(
            id = "note_4",
            title = "Cómo Reconocer una Granja de Bots",
            category = "Redes Sociales & Algoritmos",
            summary = "Perfiles creados en masa para manipular tendencias y generar clima de opinión artificial.",
            keyPoints = listOf(
                "Nombres de usuario generados automáticamente con números largos (@juan7382194).",
                "Cuentas recién creadas que publican cientos de mensajes por día sin descanso.",
                "Pocos seguidores reales pero miles de retuits clonados con el mismo hashtag.",
                "Respuestas idénticas enviadas simultáneamente a distintas figuras públicas."
            ),
            practicalTip = "Usa herramientas como Botometer o analiza el historial de la cuenta antes de creer que 'todo el mundo está hablando de eso'."
        ),
        InvestigationNote(
            id = "note_5",
            title = "El Método CRAAP de Evaluación",
            category = "Pensamiento Crítico",
            summary = "Matriz creada por bibliotecarios para juzgar la confiabilidad de cualquier texto.",
            keyPoints = listOf(
                "Currentness (Actualidad): ¿Cuándo se redactó? ¿Ha sido superado por nueva evidencia?",
                "Relevance (Relevancia): ¿Tiene relación con el tema o es relleno sensacionalista?",
                "Authority (Autoridad): ¿Quién es el autor? ¿Cuáles son sus credenciales en el área?",
                "Accuracy (Exactitud): ¿Incluye citas a fuentes primarias comprobables?",
                "Purpose (Propósito): ¿Busca informar, convencer con argumentos, o engañar con fines de propaganda/ventas?"
            ),
            practicalTip = "Si una noticia cita a 'un grupo anónimo de científicos' o 'expertos no identificados', su nivel de Autoridad y Exactitud es cero."
        ),
        InvestigationNote(
            id = "note_6",
            title = "Protocolo SIFT (Lectura Lateral)",
            category = "Técnica Profesional",
            summary = "El método de los mejores chequeadores de hechos del mundo.",
            keyPoints = listOf(
                "S - Stop (Detente): Frena tu impulso de compartir de inmediato.",
                "I - Investigate the source (Investiga la fuente): Abre otra pestaña y averigua quién es la entidad emisora.",
                "F - Find better coverage (Busca mejor cobertura): Fíjate si agencias de noticias de confianza cubrieron el hecho.",
                "T - Trace claims (Rastrea la afirmación original): Llega al video sin editar, documento oficial o estudio original."
            ),
            practicalTip = "Practica la lectura lateral: nunca te quedes dentro de la misma web que sospechas; sal a buscar qué dicen los demás sobre ella."
        )
    )

    val teacherGuideItems: List<TeacherGuideItem> = listOf(
        TeacherGuideItem(
            enigmaId = 1,
            title = "Enigma 1: Titulares Clickbait",
            evaluatedSkill = "Pensamiento crítico ante titulares sensacionalistas y detección de manipulación emocional.",
            expectedAnswer = "Titulares 1 y 4 ('¡URGENTE: Suspenderán las vacaciones!' y '¡El oscuro plan que nadie quiere que sepas!'). Emoción manipulada: MIEDO.",
            unlocksDigit = "7",
            pedagogicalExplanation = "Los estudiantes descubren que los titulares manipuladores usan signos de admiración desmedidos y apelan al miedo para forzar reacciones impulsivas.",
            commonMistake = "Confundir un titular de advertencia climática real basado en datos con un clickbait infundado."
        ),
        TeacherGuideItem(
            enigmaId = 2,
            title = "Enigma 2: Spoofing y URLs",
            evaluatedSkill = "Inspección técnica de enlaces web, certificados SSL y extensiones de dominio engañosas.",
            expectedAnswer = "URL falsa: 'http://www.min-educacion.gob.ar.portal-alumnos.xyz/comunicado'. Palabra clave: 'CERTIFICADO'.",
            unlocksDigit = "Ninguno (Desbloqueo de Firewall)",
            pedagogicalExplanation = "Enseña a los alumnos a no confiar en nombres que parecen oficiales si la extensión real es '.xyz' o carece de HTTPS.",
            commonMistake = "Mirar sólo la primera parte de la URL ('min-educacion') sin fijarse en el dominio raíz real antes de la barra."
        ),
        TeacherGuideItem(
            enigmaId = 3,
            title = "Enigma 3: Imágenes Descontextualizadas",
            evaluatedSkill = "Metodología secuencial de búsqueda inversa de imágenes y detección de anacronismos.",
            expectedAnswer = "Orden secuencial: [1, 2, 3, 4] (Guardar imagen -> Buscar en Google Lens -> Revisar fecha más antigua -> Contrastar contexto real).",
            unlocksDigit = "3",
            pedagogicalExplanation = "Comprender que las imágenes reales suelen reciclarse de otros años o países para crear noticias falsas actuales.",
            commonMistake = "Creer que si una imagen es de alta calidad fotográfica, automáticamente es verdadera y actual."
        ),
        TeacherGuideItem(
            enigmaId = 4,
            title = "Enigma 4: Granja de Bots",
            evaluatedSkill = "Detección forense de perfiles artificiales y manipulación algorítmica de tendencias.",
            expectedAnswer = "Casillas A, B y C (Fecha reciente con miles de tuits, foto generada por IA con número aleatorio, mensajes clonados en bucle).",
            unlocksDigit = "9",
            pedagogicalExplanation = "Los alumnos aprenden que los 'trending topics' pueden fabricarse artificialmente con software automatizado.",
            commonMistake = "Pensar que porque un hashtag sea número 1 en tendencias representa la opinión real de las personas."
        ),
        TeacherGuideItem(
            enigmaId = 5,
            title = "Enigma 5: Criterios CRAAP",
            evaluatedSkill = "Aplicación del marco de alfabetización informacional CRAAP.",
            expectedAnswer = "Emparejar: Actualidad <-> Fecha reciente; Autoridad <-> Credenciales del autor; Exactitud <-> Fuentes comprobables; Propósito <-> Informar vs vender/manipular.",
            unlocksDigit = "Ninguno (Sincronización de Base de Datos)",
            pedagogicalExplanation = "Brinda un marco estructurado y académico para juzgar la confiabilidad de cualquier fuente de estudio.",
            commonMistake = "Confundir el propósito comercial o de opinión de un blog con un artículo de rigor informativo."
        ),
        TeacherGuideItem(
            enigmaId = 6,
            title = "Enigma 6: Protocolo SIFT",
            evaluatedSkill = "Lectura lateral y chequeo cruzado de hechos (Fact-checking).",
            expectedAnswer = "1° DETENERSE, 2° FUENTE, 3° MEJOR, 4° ORIGINAL.",
            unlocksDigit = "2",
            pedagogicalExplanation = "Fija el protocolo moderno de verificación digital ideado por Mike Caulfield, base del periodismo de datos actual.",
            commonMistake = "Intentar verificar una página leyendo más párrafos dentro de la misma web sospechosa."
        )
    )
}
