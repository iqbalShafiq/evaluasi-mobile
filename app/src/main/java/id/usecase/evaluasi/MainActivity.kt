package id.usecase.evaluasi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    MyNavigation()
                }
            }
        }
    }
}

@Composable
fun MyNavigation() {
    val navController = rememberNavController()
    NavHost(
        startDestination = Home,
        navController = navController
    ) {
        composable<Home> {
            HomeScreenRoot(
                onClassRoomChosen = {
                    navController.navigate(ClassRoomDetail(it))
                },
                onCreateClassRoomClicked = {
                    navController.navigate(CreateClassRoom)
                }
            )
        }

        composable<CreateClassRoom> {
            CreateClassRoomScreenRoot(
                modifier = Modifier.fillMaxSize(),
                onBackPressed = {
                    navController.popBackStack()
                },
                onClassHasCreated = { classRoom ->
                    navController.navigate(CreateCategories(classRoom.id))
                }
            )
        }

        composable<ClassRoomDetail> { backStackEntry ->
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
                onStudentEditClicked = { navController.navigate(AddStudents(classRoomId)) },
                onSettingClicked = {
                    // TODO
                }
            )
        }

        composable<CreateCategories> { backStackEntry ->
            val createCategories: CreateCategories = backStackEntry.toRoute()
            val classRoomId = createCategories.classRoomId

            AddCategoriesScreenRoot(
                modifier = Modifier.fillMaxSize(),
                classRoomId = classRoomId,
                onBackPressed = {
                    navController.popBackStack()
                },
                onCategoriesHasCreated = {
                    navController.navigate(AddStudents(classRoomId))
                },
            )
        }

        composable<AddStudents> { backStackEntry ->
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

        composable<AssessmentEventEditor> { backStackEntry ->
            val assessmentEventEditor: AssessmentEventEditor = backStackEntry.toRoute()
            val classRoomId = assessmentEventEditor.classRoomId

            AssessmentScreenRoot(
                modifier = Modifier.fillMaxSize(),
                classRoomId = classRoomId,
                eventId = null,
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