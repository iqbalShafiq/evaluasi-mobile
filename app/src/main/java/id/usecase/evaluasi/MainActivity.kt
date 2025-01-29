package id.usecase.evaluasi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import id.usecase.assessment.presentation.screens.assessment.AssessmentScreenRoot
import id.usecase.assessment.presentation.screens.class_room.create.CreateClassRoomScreenRoot
import id.usecase.assessment.presentation.screens.class_room.create.categories.AddCategoriesScreenRoot
import id.usecase.assessment.presentation.screens.class_room.create.students.AddStudentsScreenRoot
import id.usecase.assessment.presentation.screens.class_room.detail.ClassRoomScreenRoot
import id.usecase.assessment.presentation.screens.home.HomeScreenRoot
import id.usecase.designsystem.EvaluasiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EvaluasiTheme {
                Column(modifier = Modifier.fillMaxSize()) {
                    MyNavigation()
                }
            }
        }
    }
}

@Composable
fun MyNavigation() {
    val navController = rememberNavController()
    val animationSpec = tween<IntOffset>(
        durationMillis = 300,
        easing = FastOutSlowInEasing
    )

    NavHost(
        startDestination = Home,
        navController = navController
    ) {
        composable<Home>(
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = animationSpec
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = animationSpec
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = animationSpec
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = animationSpec
                )
            }
        ) {
            HomeScreenRoot(
                onClassRoomChosen = {
                    navController.navigate(ClassRoomDetail(it))
                },
                onCreateClassRoomClicked = {
                    navController.navigate(
                        CreateClassRoom(classRoomId = null)
                    )
                }
            )
        }

        composable<CreateClassRoom>(
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = animationSpec
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = animationSpec
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = animationSpec
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = animationSpec
                )
            }
        ) { backStackEntry ->
            val createClassRoom: CreateClassRoom = backStackEntry.toRoute()
            val classRoomId = createClassRoom.classRoomId

            CreateClassRoomScreenRoot(
                modifier = Modifier.fillMaxSize(),
                classRoomId = classRoomId,
                onBackPressed = {
                    navController.popBackStack()
                },
                onClassRoomHasUpdated = {
                    navController.popBackStack()
                    return@CreateClassRoomScreenRoot
                },
                onClassHasCreated = { classRoom ->
                    navController.navigate(
                        CreateCategories(
                            classRoomId = classRoom.id,
                            isUpdating = false
                        )
                    )
                }
            )
        }

        composable<ClassRoomDetail>(

            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = animationSpec
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = animationSpec
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = animationSpec
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = animationSpec
                )
            }
        ) { backStackEntry ->
            val classRoomDetail: ClassRoomDetail = backStackEntry.toRoute()
            val classRoomId = classRoomDetail.classRoomId

            ClassRoomScreenRoot(
                modifier = Modifier.fillMaxSize(),
                classRoomId = classRoomId,
                onBackPressed = { navController.popBackStack() },
                onDetailAssessmentEventClicked = { assessmentEvent ->
                    navController.navigate(
                        AssessmentEventEditor(
                            classRoomId = classRoomId,
                            eventId = assessmentEvent.id
                        )
                    )
                },
                onAddAssessmentClicked = { navController.navigate(AssessmentEventEditor(classRoomId)) },
                onStudentEditClicked = {
                    navController.navigate(
                        AddStudents(
                            classRoomId = classRoomId,
                            isUpdating = true
                        )
                    )
                },
                onCategoryMenuClicked = {
                    navController.navigate(
                        CreateCategories(
                            classRoomId = classRoomId,
                            isUpdating = true
                        )
                    )
                },
                onClassRoomBioMenuClicked = {
                    navController.navigate(
                        CreateClassRoom(classRoomId = classRoomId)
                    )
                }
            )
        }

        composable<CreateCategories>(

            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = animationSpec
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = animationSpec
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = animationSpec
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = animationSpec
                )
            }
        ) { backStackEntry ->
            val createCategories: CreateCategories = backStackEntry.toRoute()
            val classRoomId = createCategories.classRoomId
            val isUpdating = createCategories.isUpdating

            AddCategoriesScreenRoot(
                modifier = Modifier.fillMaxSize(),
                classRoomId = classRoomId,
                onBackPressed = {
                    navController.popBackStack()
                },
                onCategoriesHasCreated = {
                    if (isUpdating) {
                        navController.popBackStack()
                        return@AddCategoriesScreenRoot
                    }

                    navController.navigate(
                        AddStudents(
                            classRoomId = classRoomId,
                            isUpdating = false
                        )
                    )
                },
            )
        }

        composable<AddStudents>(

            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = animationSpec
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = animationSpec
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = animationSpec
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = animationSpec
                )
            }
        ) { backStackEntry ->
            val addStudents: AddStudents = backStackEntry.toRoute()
            val classRoomId = addStudents.classRoomId

            AddStudentsScreenRoot(
                modifier = Modifier.fillMaxSize(),
                classRoomId = classRoomId,
                onBackPressed = {
                    navController.popBackStack()
                },
                onStudentHasAdded = {
                    navController.navigate(ClassRoomDetail(classRoomId)) {
                        popUpTo(Home) {
                            inclusive = false
                        }
                    }
                },
                openAutoFillScanner = {
                    // TODO
                }
            )
        }

        composable<AssessmentEventEditor>(

            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = animationSpec
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = animationSpec
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = animationSpec
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = animationSpec
                )
            }
        ) { backStackEntry ->
            val assessmentEventEditor: AssessmentEventEditor = backStackEntry.toRoute()
            val classRoomId = assessmentEventEditor.classRoomId
            val eventId = assessmentEventEditor.eventId

            AssessmentScreenRoot(
                modifier = Modifier.fillMaxSize(),
                classRoomId = classRoomId,
                eventId = eventId,
                onBackPressed = {
                    navController.popBackStack()
                },
                onAssessmentHasSaved = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EvaluasiTheme {
        MyNavigation()
    }
}